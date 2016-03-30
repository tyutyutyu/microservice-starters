package hu.bankmonitor.starter.microservice.messaging;

import hu.bankmonitor.starter.microservice.common.errorhandling.ExceptionData;
import hu.bankmonitor.starter.microservice.common.errorhandling.ExceptionType;
import hu.bankmonitor.starter.microservice.common.errorhandling.MicroserviceStarterRuntimeException;
import java.lang.reflect.InvocationTargetException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * RabbitMQ message handler services' super class.
 *
 *
 * @author istvan.foldhazi
 *
 */
@Slf4j
public abstract class AbstractMessageHandler {

	@RabbitListener(queues = "${application.messaging.queueName}")
	public final void handleMessageRouter(@Payload AbstractEvent event) {

		log.debug("handleMessageRouter - {} received with {} ID. [{}]", event.getClass().getSimpleName(), event.getId(), event);

		try {
			this.getClass().getMethod("handleMessage", event.getClass()).invoke(this, event);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			// @formatter:off
			throw new MicroserviceStarterRuntimeException(ExceptionData.builder()
					.type(ExceptionType.RABBITMQ_MESSAGE_HANDLING_ERROR)
					.cause(e)
					.build());
			// @formatter:on
		}
	}

}
