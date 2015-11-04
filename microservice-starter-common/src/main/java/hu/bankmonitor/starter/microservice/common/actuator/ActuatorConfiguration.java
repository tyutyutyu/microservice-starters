package hu.bankmonitor.starter.microservice.common.actuator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActuatorConfiguration {

	@Bean
	@SuppressWarnings("static-method")
	SystemDateTimeIndicator systemDateTime() {

		return new SystemDateTimeIndicator();
	}

}
