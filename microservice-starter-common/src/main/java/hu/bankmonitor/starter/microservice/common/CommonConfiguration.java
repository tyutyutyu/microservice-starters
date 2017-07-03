package hu.bankmonitor.starter.microservice.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {

	@Bean
	@SuppressWarnings("static-method")
	ProfileLogger profileReporter() {

		return new ProfileLogger();
	}

}
