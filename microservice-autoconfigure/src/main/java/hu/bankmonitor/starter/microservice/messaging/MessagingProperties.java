package hu.bankmonitor.starter.microservice.messaging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.messaging")
@Getter
@NoArgsConstructor
@Setter
@ToString
public class MessagingProperties {

	/**
	 * The application's name. This is added to all messages.
	 */
	@Value("${spring.application.name}")
	private String applicationName;

	private String queueName;

	private String exchangeName;

	private String rpcQueueName;
}
