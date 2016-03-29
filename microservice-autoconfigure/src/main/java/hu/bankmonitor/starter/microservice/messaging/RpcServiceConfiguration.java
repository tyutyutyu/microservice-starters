package hu.bankmonitor.starter.microservice.messaging;

import com.google.common.collect.ImmutableMap;
import hu.bankmonitor.starter.microservice.common.errorhandling.ExceptionData;
import hu.bankmonitor.starter.microservice.common.errorhandling.ExceptionType;
import hu.bankmonitor.starter.microservice.common.errorhandling.MicroserviceStarterRuntimeException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.remoting.client.AmqpProxyFactoryBean;
import org.springframework.amqp.remoting.service.AmqpInvokerServiceExporter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

@Component
@Slf4j
public class RpcServiceConfiguration {

	private static final String AMQP_PROXY_SUFFIX = "_AMQP_PROXY";

	private static final String AMQP_SERVICE_EXPORTER_SUFFIX = "_SERVICE_EXPORTER";

	private static final String AMQP_LISTENER_SUFFIX = "_AMQP_LISTENER";

	private static final String BASE_PACKAGE = "hu";

	private BeanDefinitionRegistry registry;

	@Autowired
	private AmqpAdmin amqpAdmin;

	@Autowired
	private ConfigurableApplicationContext applicationContext;

	@Autowired
	private ConnectionFactory connectionFactory;

	@Autowired(required = false)
	private List<RpcService> rpcServices;

	@Autowired
	private MessageConverter messageConverter;

	@Autowired
	private MessagingProperties properties;

	@PostConstruct
	public void init() {

		log.debug("init - ");

		registry = (BeanDefinitionRegistry) applicationContext.getBeanFactory();

		registerRpcServers();
		registerRpcClients();
	}

	private void registerRpcClients() {

		AnnotatedInterfaceProvider interfaceProvider = new AnnotatedInterfaceProvider(false);
		interfaceProvider.addIncludeFilter(new AnnotationTypeFilter(RpcServiceClient.class));
		for (BeanDefinition bd : interfaceProvider.findCandidateComponents(BASE_PACKAGE)) {

			String proxyName = bd.getBeanClassName() + AMQP_PROXY_SUFFIX;

			try {
				Class<?> serviceClass = Class.forName(bd.getBeanClassName());
				BeanDefinitionBuilder amqpProxyFactoryBuilder = BeanDefinitionBuilder.rootBeanDefinition(AmqpProxyFactoryBean.class);
				amqpProxyFactoryBuilder.addPropertyValue("serviceInterface", serviceClass);
				amqpProxyFactoryBuilder.addDependsOn("rabbitTemplate");
				amqpProxyFactoryBuilder.addPropertyReference("amqpTemplate", "rabbitTemplate");
				amqpProxyFactoryBuilder.addPropertyValue("routingKey", serviceClass.getName());
				registry.registerBeanDefinition(proxyName, amqpProxyFactoryBuilder.getBeanDefinition());
				log.debug("registerRpcClients - {} with name {} was registered.", amqpProxyFactoryBuilder.getBeanDefinition().getBeanClassName(), proxyName);

			} catch (ClassNotFoundException e) {
				throw new MicroserviceStarterRuntimeException(ExceptionData.builder().type(ExceptionType.RABBITMQ_RPC_INIT_ERROR).message("Amqp proxy service cannot be created")
						.cause(e).data(ImmutableMap.of("className", bd.getBeanClassName())).build());
			}

		}
	}

	private void registerRpcServers() {

		if (rpcServices != null) {
			log.debug("registerRpcServers - rpc services to register: {}", rpcServices);

			for (RpcService rpcService : rpcServices) {

				Class<?> rpcServiceInterface;
				try (Stream<Class<?>> stream = Arrays.stream(ClassUtils.getAllInterfaces(rpcService))) {
					rpcServiceInterface = stream.filter(c -> c.getAnnotation(RpcServiceServer.class) != null).findFirst().get();
				}

				BeanDefinitionBuilder amqpServiceExporterBuilder = BeanDefinitionBuilder.genericBeanDefinition(AmqpInvokerServiceExporter.class);
				amqpServiceExporterBuilder.addPropertyReference("amqpTemplate", "rabbitTemplate");
				amqpServiceExporterBuilder.addPropertyValue("messageConverter", messageConverter);
				amqpServiceExporterBuilder.addPropertyValue("service", rpcService);
				amqpServiceExporterBuilder.addPropertyValue("serviceInterface", rpcServiceInterface);
				String beanName = rpcServiceInterface.getName() + AMQP_SERVICE_EXPORTER_SUFFIX;
				registry.registerBeanDefinition(beanName, amqpServiceExporterBuilder.getBeanDefinition());

				BeanDefinitionBuilder messageListenerContainerBuilder = BeanDefinitionBuilder.genericBeanDefinition(SimpleMessageListenerContainer.class);
				messageListenerContainerBuilder.addPropertyValue("connectionFactory", connectionFactory);
				messageListenerContainerBuilder.addPropertyReference("messageListener", beanName);
				messageListenerContainerBuilder.addPropertyValue("messageConverter", messageConverter);
				messageListenerContainerBuilder.addPropertyValue("queueNames", properties.getRpcQueueName());
				registry.registerBeanDefinition(rpcServiceInterface.getName() + AMQP_LISTENER_SUFFIX, messageListenerContainerBuilder.getBeanDefinition());

				log.debug("registerRpcServers - {} with name {} was registered.", amqpServiceExporterBuilder.getBeanDefinition().getBeanClassName(), beanName);
				amqpAdmin.declareBinding(
						BindingBuilder.bind(new Queue(properties.getRpcQueueName())).to(new DirectExchange(properties.getExchangeName())).with(rpcServiceInterface.getName()));
			}
		}
	}

}
