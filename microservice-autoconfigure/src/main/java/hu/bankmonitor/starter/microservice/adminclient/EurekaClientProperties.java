package hu.bankmonitor.starter.microservice.adminclient;

import lombok.Setter;
import lombok.ToString;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "eureka", exceptionIfInvalid = false)
@Setter
@ToString
public class EurekaClientProperties {

	/**
	 * Az Admin Server URL-je, plusz a service URI-je. Pl.: http://localhost:8761/v2/
	 */
	@Value("${eureka.serviceUrl.default}")
	private String serviceUrl;

	/**
	 * Az alkalmazás neve, ami majd megjelenik az Admin Server-en
	 */
	@Value("${spring.application.name}")
	private String applicationName;

}
