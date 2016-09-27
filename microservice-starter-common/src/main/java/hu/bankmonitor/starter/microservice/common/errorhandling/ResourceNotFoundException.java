package hu.bankmonitor.starter.microservice.common.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends MicroserviceStarterRuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(ExceptionData exceptionData) {
		super(exceptionData);
	}

}
