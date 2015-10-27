package hu.bankmonitor.starter.microservice.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class MessageService {

	@Autowired
	private MessagePostProcessor messagePostProcessor;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * Sends message to RabbitMQ
	 *
	 * @param event
	 *            The message
	 */
	public void send(AbstractEvent event) {

		send(event, event.getId());
	}

	/**
	 * Sends message to RabbitMQ
	 *
	 * @param payload
	 *            The message
	 *
	 * @param identifier
	 *            The message's identifier
	 */
	public void send(Object payload, String identifier) {

		String routingKey = payload.getClass().getCanonicalName();

		log.debug("Sending message - routingKey: {}, id: {}, payload: {}", routingKey, identifier, payload);

		rabbitTemplate.convertAndSend(routingKey, payload, messagePostProcessor, new CorrelationData(identifier));
	}

	/**
	 * Sends message to RabbitMQ and waits for response
	 *
	 * @param payload
	 *            The message
	 * @return response from RabbitMQ
	 *
	 */
	public Object sendAndReceive(Object payload) {

		String routingKey = payload.getClass().getCanonicalName();

		log.debug("Sending message - routingKey: {}, payload: {}", routingKey, payload);

		return rabbitTemplate.convertSendAndReceive(routingKey, payload, messagePostProcessor);

	}
}
