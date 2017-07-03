package hu.bankmonitor.starter.microservice.common;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

@Slf4j
public class ProfileLogger {

	@Autowired
	private Environment env;

	/**
	 * Logging the active profiles at startup
	 */
	@PostConstruct
	public void postConstruct() {

		if (env.getActiveProfiles().length == 0) {
			log.warn("No Spring profile configured, running with default configuration");
		} else {
			log.info("Running with Spring profile(s): {}", (Object) env.getActiveProfiles());
		}
	}

}
