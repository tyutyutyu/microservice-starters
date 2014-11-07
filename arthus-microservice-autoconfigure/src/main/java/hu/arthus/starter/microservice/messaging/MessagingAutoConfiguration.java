package hu.arthus.starter.microservice.messaging;

import hu.arthus.starter.microservice.messaging.EventClassFinderConfiguration.EventClassFinder;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.aop.aspectj.TypePatternClassFilter;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
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

		createBindings();

		log.debug("MessagingAutoConfiguration loaded. [{}]", properties);
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
	RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {

		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setExchange(properties.getExchangeName());
		template.setQueue(properties.getQueueName());
		template.setMessageConverter(new Jackson2JsonMessageConverter());
		return template;
	}

	// @Bean
	// MessageListenerAdapter listenerAdapter(EventBus eventBus) {
	//
	// return new MessageListenerAdapter(eventBus, "onMessage");
	// }

	// @Bean
	// SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
	//
	// SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
	// container.setConnectionFactory(connectionFactory);
	// container.setQueueNames(properties.getQueueName());
	// container.setMessageListener(listenerAdapter);
	// return container;
	// }

	@Bean
	public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, @Qualifier("messageListener") MessageListener messageListener) {

		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
		container.setQueueNames(properties.getQueueName());
		container.setMessageListener(messageListener);
		return container;
	}

	@Bean
	@ConditionalOnMissingClass(name = "org.axonframework.eventhandling.annotation.EventHandler")
	public MessageListenerAdapter messageListener(@Qualifier("messageListener") Object delegate) {

		return new MessageListenerAdapter(delegate, new Jackson2JsonMessageConverter());
	}

	// TODO: FI: külön configba
	// @Bean
	NameMatchMethodPointcutAdvisor matchMethodPointcutAdvisor() {

		NameMatchMethodPointcutAdvisor advisor = new NameMatchMethodPointcutAdvisor();
		advisor.setClassFilter(new TypePatternClassFilter("org.springframework.amqp.rabbit.core.RabbitTemplate"));
		advisor.setMappedName("convertAndSend");
		advisor.setAdvice(new MethodInterceptor() {

			@Override
			@SuppressWarnings("synthetic-access")
			public Object invoke(MethodInvocation invocation) throws Throwable {

				log.debug("invocation: {}", invoke(invocation));

				return null;
			}
		});

		return advisor;
	}
}
