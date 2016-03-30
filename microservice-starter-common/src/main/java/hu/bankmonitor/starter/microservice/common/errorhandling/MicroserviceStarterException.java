package hu.bankmonitor.starter.microservice.common.errorhandling;

import lombok.Getter;

@Getter
public class MicroserviceStarterException extends Exception implements ExceptionWithExceptionContext {

	private static final long serialVersionUID = 1L;

	private final ExceptionContext exceptionContext;

	public MicroserviceStarterException(ExceptionData exceptionData) {

		super(exceptionData.getCause());

		exceptionContext = ExceptionContext.create(exceptionData);
	}

}
