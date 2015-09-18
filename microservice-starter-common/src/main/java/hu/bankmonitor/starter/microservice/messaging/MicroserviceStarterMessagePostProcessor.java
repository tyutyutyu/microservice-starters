package hu.bankmonitor.starter.microservice.messaging;

import java.util.Date;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

import lombok.AllArgsConstructor;

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
