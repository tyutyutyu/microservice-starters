package hu.bankmonitor.starter.microservice.messaging;

import java.util.UUID;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class AbstractEvent {

	private String id;

	public AbstractEvent() {

		id = UUID.randomUUID().toString();
	}

}
