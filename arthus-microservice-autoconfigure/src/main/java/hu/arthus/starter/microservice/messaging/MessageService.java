package hu.arthus.starter.microservice.messaging;

import java.util.Date;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;

public class MessageService {

	class ArthusMessagePostProcessor implements MessagePostProcessor {

		ArthusMessagePostProcessor() {

		}

		@Override
		public Message postProcessMessage(Message message) throws AmqpException {

			message.getMessageProperties().setAppId(messagingProperties.getApplicationName());
			message.getMessageProperties().setTimestamp(new Date());

			return message;
		}

	}

	final MessagePostProcessor messagePostProcessor = new ArthusMessagePostProcessor();

	@Autowired
	private MessagingProperties messagingProperties;

	@Autowired
	private RabbitTemplate rabbitTemplate;

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
