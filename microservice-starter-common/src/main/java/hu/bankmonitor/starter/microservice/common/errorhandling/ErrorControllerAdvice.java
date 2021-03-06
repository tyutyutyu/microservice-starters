package hu.bankmonitor.starter.microservice.common.errorhandling;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorControllerAdvice {

	@ExceptionHandler(Throwable.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@SuppressWarnings("static-method")
	public RestErrorMessage handleThrowable(HttpServletRequest request, Exception exception) {

		LogUtils.logException(exception, request);

		return RestErrorMessage.create();
	}

	@ExceptionHandler(MicroserviceStarterException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@SuppressWarnings("static-method")
	public RestErrorMessage handleMicroserviceStarterException(HttpServletRequest request, MicroserviceStarterException exception) {

		LogUtils.logException(exception, request);

		return RestErrorMessage.create(exception);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@SuppressWarnings("static-method")
	public RestErrorMessage handleResourceNotFoundException(HttpServletRequest request, ResourceNotFoundException exception) {

		LogUtils.logException(exception, request);

		return RestErrorMessage.create(exception);
	}

	@ExceptionHandler(MicroserviceStarterRuntimeException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@SuppressWarnings("static-method")
	public RestErrorMessage handleMicroserviceStarterRuntimeException(HttpServletRequest request, MicroserviceStarterRuntimeException exception) {

		LogUtils.logException(exception, request);

		return RestErrorMessage.create(exception);
	}

}
