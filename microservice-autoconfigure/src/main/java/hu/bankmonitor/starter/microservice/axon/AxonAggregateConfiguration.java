package hu.bankmonitor.starter.microservice.axon;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandlerFactoryBean;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.mongo.MongoEventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.ClassUtils;

/**
 *
 * @author Istvan Foldhazi
 */
@ConditionalOnClass({ MongoEventStore.class })
@Configuration
@Slf4j
public class AxonAggregateConfiguration {

	private static final String BASE_PACKAGE = "hu.bankmonitor";

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private CommandBus commandBus;

	@Autowired
	private EventStore eventStore;

	@Autowired
	private EventBus eventBus;

	@PostConstruct
	public void init() {

		ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(false);
		componentProvider.addIncludeFilter(new AssignableTypeFilter(AbstractAnnotatedAggregateRoot.class));

		for (BeanDefinition candidate : componentProvider.findCandidateComponents(BASE_PACKAGE)) {
			try {
				log.debug("Found aggregate: {}", candidate.getBeanClassName());
				initAggregateConfig(candidate.getBeanClassName());
			} catch (ClassNotFoundException | LinkageError e) {
				// TODO: review exception handling
				log.error("ClassPathScanningCandidateComponentProvider found class {} but ClassLoader could not load it.", candidate.getBeanClassName());
				log.error("Exception: {}", e);
			}
		}

		log.debug("AxonAggregateConfiguration loaded.");
	}

	private void initAggregateConfig(String className) throws ClassNotFoundException, LinkageError {

		Class<?> clazz = ClassUtils.forName(className, getClass().getClassLoader());

		try {
			Object beanObject = aggregateAnnotationCommandHandler(clazz);
			applicationContext.getAutowireCapableBeanFactory().autowireBean(beanObject);
			log.debug("CommandHandler registered for {}", clazz.getTypeName());
		} catch (Exception e) {
			// TODO: review exception handling
			log.error("Exception: {}", e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private AggregateAnnotationCommandHandler aggregateAnnotationCommandHandler(Class clazz) throws Exception {

		final EventSourcingRepository repository = new EventSourcingRepository(clazz, eventStore);
		repository.setEventBus(eventBus);

		final AggregateAnnotationCommandHandlerFactoryBean factoryBean = new AggregateAnnotationCommandHandlerFactoryBean<>();
		factoryBean.setRepository(repository);
		factoryBean.setCommandBus(commandBus);
		factoryBean.setAggregateType(clazz);
		factoryBean.afterPropertiesSet();

		return factoryBean.getObject();
	}

}
