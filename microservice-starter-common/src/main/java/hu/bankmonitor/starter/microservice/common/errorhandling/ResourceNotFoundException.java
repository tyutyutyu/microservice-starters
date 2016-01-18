package hu.bankmonitor.starter.microservice.common.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author nnonn
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException() {
		super();
	}

	@SuppressWarnings("ucd")
	public ResourceNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	@SuppressWarnings("ucd")
	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	@SuppressWarnings("ucd")
	public ResourceNotFoundException(String message) {
		super(message);
	}

	@SuppressWarnings("ucd")
	public ResourceNotFoundException(Throwable cause) {
		super(cause);
	}

}
