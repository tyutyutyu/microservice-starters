package hu.bankmonitor.starter.microservice.common.exception;

public class MicroserviceStarterRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MicroserviceStarterRuntimeException() {
		super();
	}

	public MicroserviceStarterRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MicroserviceStarterRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public MicroserviceStarterRuntimeException(String message) {
		super(message);
	}

	public MicroserviceStarterRuntimeException(Throwable cause) {
		super(cause);
	}

}
