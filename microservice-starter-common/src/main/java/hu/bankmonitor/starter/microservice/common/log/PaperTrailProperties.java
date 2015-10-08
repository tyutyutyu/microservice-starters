package hu.bankmonitor.starter.microservice.common.log;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.paper-trail")
@Getter
@Setter
public class PaperTrailProperties {

	/**
	 * PaperTrail host
	 */
	private String host;

	/**
	 * PaperTrail port
	 */
	private int port;

	/**
	 * PaperTrail application identifier
	 */
	private String ident;

	/**
	 * PaperTrail logging pattern
	 */
	private String pattern = "%thread: %-5p %-40.40logger{39}: %m%n%wex";

	/**
	 * Max message length
	 */
	private int maxMessageLength = 128000;

}
