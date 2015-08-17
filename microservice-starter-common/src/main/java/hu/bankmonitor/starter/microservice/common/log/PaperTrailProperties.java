package hu.bankmonitor.starter.microservice.common.log;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "application.paper-trail")
@Getter
@Setter
public class PaperTrailProperties {

	private String host;

	private int port;

	private String ident;

	private String pattern;

	private int maxMessageLength = 128000;

}
