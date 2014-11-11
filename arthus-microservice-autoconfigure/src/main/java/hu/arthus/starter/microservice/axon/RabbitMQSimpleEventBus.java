package hu.arthus.starter.microservice.axon;

import hu.arthus.starter.microservice.messaging.MessageService;

import lombok.extern.slf4j.Slf4j;

import org.axonframework.domain.EventMessage;
import org.axonframework.domain.GenericDomainEventMessage;
import org.axonframework.eventhandling.SimpleEventBus;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 *
 * @author zoltan.puskai
 */
@Slf4j
public class RabbitMQSimpleEventBus extends SimpleEventBus implements MessageListener {

	private final RabbitTemplate rabbitTemplate;

	private final MessageService messageService;

	public RabbitMQSimpleEventBus(RabbitTemplate rabbitTemplate, MessageService messageService) {

		this.rabbitTemplate = rabbitTemplate;
		this.messageService = messageService;
	}

	@Override
	public void publish(@SuppressWarnings("rawtypes") EventMessage... events) {

		for (EventMessage<?> event : events) {
			log.debug("[PUBLISH     ] New event is published. [event={}]", event);
			messageService.send(event.getPayload(), event.getIdentifier());
		}
	}

	@Override
	public void onMessage(Message message) {

		EventMessage<?> event = new GenericDomainEventMessage<>(message.getMessageProperties().getCorrelationId(), 0, rabbitTemplate.getMessageConverter().fromMessage(message));
		log.debug("[HANDLE_EVENT] New event handled. [event={}]", event);
		super.publish(event);
	}

}
