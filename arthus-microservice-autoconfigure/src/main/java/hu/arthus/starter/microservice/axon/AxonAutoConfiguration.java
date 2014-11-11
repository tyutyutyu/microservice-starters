package hu.arthus.starter.microservice.axon;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import hu.arthus.starter.microservice.messaging.MessageService;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerBeanPostProcessor;
import org.axonframework.commandhandling.gateway.CommandGatewayFactoryBean;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerBeanPostProcessor;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.mongo.DefaultMongoTemplate;
import org.axonframework.eventstore.mongo.MongoEventStore;
import org.axonframework.eventstore.mongo.MongoTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ConditionalOnClass({ MongoEventStore.class })
@Configuration
@Import(AxonAggregateConfiguration.class)
@Slf4j
@SuppressWarnings("static-method")
public class AxonAutoConfiguration {

	@PostConstruct
	void init() {

		log.debug("AxonAutoConfiguration loaded.");
	}

	@Bean
	AnnotationEventListenerBeanPostProcessor annotationEventListenerBeanPostProcessor() {

		return new AnnotationEventListenerBeanPostProcessor();
	}

	@Bean
	AnnotationCommandHandlerBeanPostProcessor annotationCommandHandlerBeanPostProcessor() {

		return new AnnotationCommandHandlerBeanPostProcessor();
	}

	@Bean
	CommandBus commandBus() {

		return new SimpleCommandBus();
	}

	@Bean
	CommandGatewayFactoryBean<DefaultCommandGateway> commandGateway(CommandBus commandBus) {

		final CommandGatewayFactoryBean<DefaultCommandGateway> factoryBean = new CommandGatewayFactoryBean<>();
		factoryBean.setCommandBus(commandBus);

		return factoryBean;
	}

	@Bean
	EventStore eventStore() throws UnknownHostException {

		Mongo client = new MongoClient("localhost");
		// TODO: FI: databaseName beállítás
		MongoTemplate mongo = new DefaultMongoTemplate(client);
		return new MongoEventStore(mongo);
	}

	@Bean
	@Qualifier("messageListener")
	EventBus eventBus(RabbitTemplate rabbitTemplate, MessageService messageService) {

		return new RabbitMQSimpleEventBus(rabbitTemplate, messageService);
	}

}
