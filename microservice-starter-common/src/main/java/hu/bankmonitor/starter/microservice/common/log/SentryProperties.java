package hu.bankmonitor.starter.microservice.common.log;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "microservice-starters.sentry")
@Getter
@Setter
public class SentryProperties {

	/**
	 * Sentry DSN
	 */
	private String dsn;

}
