package hu.bankmonitor.starter.microservice.messaging;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * RabbitMQ message class' super class.
 *
 * @author istvan.foldhazi
 *
 */
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public abstract class AbstractEvent {

	private String id;

	public AbstractEvent() {

		id = UUID.randomUUID().toString();
	}

}
