package hu.arthus.starter.microservice.messaging;

import java.lang.reflect.InvocationTargetException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;

@Slf4j
public abstract class AbstractMessageHandler {

	@RabbitListener(queues = "${arthus.messaging.queueName}")
	public void handleMessage(@Payload AbstractEvent event) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException {

		log.debug("{} received - id: {}", event.getClass().getSimpleName(), event.getId());

		this.getClass().getMethod("handleMessage", event.getClass()).invoke(this, event);
	}

}
