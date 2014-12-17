package hu.arthus.starter.microservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import hu.arthus.starter.microservice.messaging.EventClassFinderConfiguration.EventClassFinder;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnClass({ RabbitAdmin.class })
@Configuration
@EnableConfigurationProperties(MessagingProperties.class)
@Slf4j
@SuppressWarnings("static-method")
public class MessagingAutoConfiguration {

	@Autowired
	private MessagingProperties properties;

	@Autowired
	private AmqpAdmin amqpAdmin;

	@Autowired
	private EventClassFinder eventClassFinder;

	@PostConstruct
	void init() {

		log.debug("MessagingAutoConfiguration loaded. [properties={}, eventClassFinder={}]", properties, eventClassFinder);

		createBindings();

	}

	/**
	 * Az alkalmazás queue-ja, ebből olvassa ki az alkalmazás a számára fontos üzeneteket
	 *
	 * @return
	 */
	@Bean
	Queue queue() {

		return new Queue(properties.getQueueName(), true, false, false);
	}

	/**
	 * A közös exchange, ahova minden service küldi az event-eket
	 *
	 * @return
	 */
	@Bean
	TopicExchange exchange() {

		return new TopicExchange(properties.getExchangeName(), true, false);
	}

	@Bean
	EventClassFinderConfiguration listenerFinderService() {

		return new EventClassFinderConfiguration();
	}

	private void createBindings() {

		for (String className : eventClassFinder.findEventClasses()) {
			amqpAdmin.declareBinding(BindingBuilder.bind(queue()).to(exchange()).with(className));
		}
	}

	@Bean
	Jackson2JsonMessageConverter messageConverter() {

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

	@Configuration
	public static class RabbitListenerConfigurerImpl implements RabbitListenerConfigurer {

		@Autowired
		ConnectionFactory connectionFactory;

		@Autowired
		Jackson2JsonMessageConverter messageConverter;

		@Override
		public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {

			SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
			containerFactory.setConnectionFactory(connectionFactory);
			containerFactory.setMessageConverter(messageConverter);

			// Hogy újra lehessen küldeni az üzeneteket többször is az EventStore-ból
			containerFactory.setAcknowledgeMode(AcknowledgeMode.NONE);

			registrar.setContainerFactory(containerFactory);
		}

	}

}
