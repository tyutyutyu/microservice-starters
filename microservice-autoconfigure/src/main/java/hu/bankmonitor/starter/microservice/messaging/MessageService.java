package hu.bankmonitor.starter.microservice.messaging;

import java.util.Date;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;

public class MessageService {

	class MicroserviceStarterMessagePostProcessor implements MessagePostProcessor {

		MicroserviceStarterMessagePostProcessor() {

		}

		@Override
		public Message postProcessMessage(Message message) throws AmqpException {

			message.getMessageProperties().setAppId(messagingProperties.getApplicationName());
			message.getMessageProperties().setTimestamp(new Date());

			return message;
		}

	}

	final MessagePostProcessor messagePostProcessor = new MicroserviceStarterMessagePostProcessor();

	@Autowired
	private MessagingProperties messagingProperties;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * Üzenet küldése a RabbitMQ felé
	 *
	 * @param event
	 *            Az üzenet
	 */
	public void send(AbstractEvent event) {

		String routingKey = event.getClass().getCanonicalName();

		rabbitTemplate.convertAndSend(routingKey, event, messagePostProcessor, new CorrelationData(event.getId()));
	}

	/**
	 * Üzenet küldése a RabbitMQ felé
	 *
	 * @param payload
	 *            Az üzenet
	 * @param identifier
	 *            Egyedi üzenet azonosító
	 */
	public void send(Object payload, String identifier) {

		String routingKey = payload.getClass().getCanonicalName();

		rabbitTemplate.convertAndSend(routingKey, payload, messagePostProcessor, new CorrelationData(identifier));
	}

}