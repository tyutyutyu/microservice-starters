package hu.bankmonitor.starter.microservice.common.log;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "microservice-starters.sentry")
@Getter
@Setter
public class SentryProperties {

	/**
	 * @see io.sentry.Sentry
	 */
	private String dsn;

	/**
	 * @see io.sentry.Sentry
	 */
	private String dsnPublic;

	/**
	 * @see ch.qos.logback.classic.filter.ThresholdFilter
	 */
	private String logLevel = "ERROR";

}
