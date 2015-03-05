package hu.arthus.starter.microservice.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {

	@Bean
	@SuppressWarnings("static-method")
	ProfileReporter profileReporter() {

		return new ProfileReporter();
	}

}
