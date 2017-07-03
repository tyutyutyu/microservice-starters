package hu.bankmonitor.starter.microservice.common.errorhandling;

import lombok.Getter;

@Getter
public class MicroserviceStarterRuntimeException extends RuntimeException implements ExceptionWithExceptionContext {

	private static final long serialVersionUID = 1L;

	private final ExceptionContext exceptionContext;

	public MicroserviceStarterRuntimeException(ExceptionData exceptionData) {

		super(exceptionData.getCause());

		exceptionContext = ExceptionContext.create(exceptionData);
	}

}
