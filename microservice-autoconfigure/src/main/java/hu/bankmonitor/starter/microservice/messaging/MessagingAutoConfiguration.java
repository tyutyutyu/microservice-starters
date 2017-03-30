package hu.bankmonitor.starter.microservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnClass({ RabbitAdmin.class })
@Configuration
@EnableConfigurationProperties(MessagingProperties.class)
@Slf4j
@SuppressWarnings("static-method")
public class MessagingAutoConfiguration {

	private static final String BASE_PACKAGE = "hu";

	@Configuration
	public static class RabbitListenerConfigurerImpl implements RabbitListenerConfigurer {

		@Autowired
		ConnectionFactory connectionFactory;

		@Autowired
		MessageConverter messageConverter;

		@Override
		public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {

			SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
			containerFactory.setConnectionFactory(connectionFactory);
			containerFactory.setMessageConverter(messageConverter);

			// For resending messages from the EventStore
			containerFactory.setAcknowledgeMode(AcknowledgeMode.NONE);

			registrar.setContainerFactory(containerFactory);
		}
	}

	@Autowired
	private AmqpAdmin amqpAdmin;

	@Autowired
	private MessagingProperties properties;

	@PostConstruct
	void init() {

		log.debug("MessagingAutoConfiguration loaded. [properties={}]", properties);

		createBindings();

	}

	/**
	 * The application's queue
	 *
	 * @return
	 */
	@Bean
	Queue queue() {

		return new Queue(properties.getQueueName(), true, false, false);
	}

	/**
	 * The application's rpc queue
	 *
	 * @return
	 */
	@Bean
	@ConditionalOnProperty("application.messaging.rpc-queue-name")
	Queue rpcQueue() {

		return new Queue(properties.getRpcQueueName(), true, false, false);
	}

	/**
	 * The common exchange
	 *
	 * @return
	 */
	@Bean
	TopicExchange exchange() {

		return new TopicExchange(properties.getExchangeName(), true, false);
	}

	@Bean
	RpcServiceConfiguration rpcServiceConfiguration() {

		return new RpcServiceConfiguration();
	}

	private void createBindings() {

		EventClassFinder eventClassFinder = new EventClassFinder(BASE_PACKAGE);
		for (String className : eventClassFinder.findEventClasses()) {
			amqpAdmin.declareBinding(BindingBuilder.bind(queue()).to(exchange()).with(className));
		}
	}

	@Bean
	MessageConverter messageConverter() {

		Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
		ObjectMapper jsonObjectMapper = new ObjectMapper();
		jsonObjectMapper.registerModule(new JodaModule());
		messageConverter.setJsonObjectMapper(jsonObjectMapper);

		return messageConverter;
	}

	@Bean
	RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {

		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setExchange(properties.getExchangeName());
		template.setQueue(properties.getQueueName());
		template.setMessageConverter(messageConverter());

		return template;
	}

	@Bean
	MessageService messageService() {

		return new MessageService();
	}

	@Bean
	MicroserviceStarterMessagePostProcessor messagePostProcessor() {

		return new MicroserviceStarterMessagePostProcessor(properties.getApplicationName());
	}

}
