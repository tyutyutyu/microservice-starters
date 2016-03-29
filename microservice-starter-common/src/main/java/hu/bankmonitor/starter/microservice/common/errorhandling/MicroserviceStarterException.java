package hu.bankmonitor.starter.microservice.common.errorhandling;

import java.util.UUID;
import lombok.Getter;

@Getter
public class MicroserviceStarterException extends Exception implements ExceptionWithExceptionData {

	private static final long serialVersionUID = 1L;

	private String id = UUID.randomUUID().toString();

	private final ExceptionData exceptionData;

	public MicroserviceStarterException(ExceptionData exceptionData) {

		super(exceptionData.getMessage(), exceptionData.getCause());
		this.exceptionData = exceptionData;
	}

}
