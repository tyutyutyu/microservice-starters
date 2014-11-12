package hu.arthus.starter.microservice.messaging;

import java.util.UUID;

import lombok.Getter;

@Getter
public abstract class AbstractEvent {

	private String id;

	public AbstractEvent() {

		id = UUID.randomUUID().toString();
	}

}
