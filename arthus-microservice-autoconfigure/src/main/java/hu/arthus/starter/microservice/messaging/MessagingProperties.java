package hu.arthus.starter.microservice.messaging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "arthus.messaging")
@Getter
@NoArgsConstructor
@Setter
@ToString
public class MessagingProperties {

	/**
	 * Az alkalmazás queue-jának a neve
	 */
	private String queueName;

	/**
	 * A közös exchange neve
	 */
	private String exchangeName;

}
