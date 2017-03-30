package hu.bankmonitor.starter.microservice.messaging;

import java.util.Date;
import lombok.AllArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

/**
 * RabbitMQ message post processor. Adds the necessary informations to messages.
 *
 * @author istvan.foldhazi
 *
 */
@AllArgsConstructor
public class MicroserviceStarterMessagePostProcessor implements MessagePostProcessor {

	private String applicationName;

	@Override
	public Message postProcessMessage(Message message) throws AmqpException {

		message.getMessageProperties().setAppId(applicationName);
		message.getMessageProperties().setTimestamp(new Date());

		return message;
	}

}
