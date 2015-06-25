package hu.bankmonitor.starter.microservice.messaging;

import java.lang.reflect.InvocationTargetException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractMessageHandler {

	@RabbitListener(queues = "${application.messaging.queueName}")
	public void handleMessageRouter(@Payload AbstractEvent event)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		log.debug("handleMessageRouter - {} received with {} ID. [{}]", event.getClass().getSimpleName(), event.getId(), event);

		this.getClass().getMethod("handleMessage", event.getClass()).invoke(this, event);
	}

}
