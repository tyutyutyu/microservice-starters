package hu.bankmonitor.starter.microservice.common;

import hu.bankmonitor.starter.microservice.common.errorhandling.ErrorControllerAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorHandlingAutoConfiguration {

	@Bean
	@SuppressWarnings("static-method")
	ErrorControllerAdvice errorControllerAdvice() {

		return new ErrorControllerAdvice();
	}

}
