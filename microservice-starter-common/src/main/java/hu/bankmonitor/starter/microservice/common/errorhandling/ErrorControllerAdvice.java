package hu.bankmonitor.starter.microservice.common.errorhandling;

import hu.bankmonitor.starter.microservice.common.log.LogUtils;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ErrorControllerAdvice {

	private static final String ERROR_VIEW_404 = "errors/404";

	private static final String ERROR_VIEW_500 = "errors/500";

	@Deprecated
	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@SuppressWarnings("static-method")
	public String handleThrowable(HttpServletRequest request, Exception e) {

		LogUtils.logException(log, "handleThrowable", request, e);

		return ERROR_VIEW_500;
	}

	@Deprecated
	@ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@SuppressWarnings("static-method")
	public String handleUnsatisfiedServletRequestParameterException(HttpServletRequest request, UnsatisfiedServletRequestParameterException e) {

		LogUtils.logException(log, "handleUnsatisfiedServletRequestParameterException", request, e);

		return ERROR_VIEW_500;
	}

	@Deprecated
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@SuppressWarnings("static-method")
	public String handleResourceNotFoundException(HttpServletRequest request, Exception e) {

		log.debug("handleResourceNotFoundException - request.url: {}, exception.message: {}", request.getRequestURL(), e.getMessage());

		return ERROR_VIEW_404;
	}

	@Deprecated
	@ExceptionHandler(BankmonitorException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@SuppressWarnings("static-method")
	public String handleBankmonitorException(HttpServletRequest request, BankmonitorException e) {

		LogUtils.logException(log, "handleBankmonitorException", request, e);

		return ERROR_VIEW_500;
	}

	@ExceptionHandler(BankmonitorRestException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@SuppressWarnings("static-method")
	public RestErrorMessage handleBankmonitorRestException(HttpServletRequest request, BankmonitorRestException e) {

		LogUtils.logException(log, "handleBankmonitorException", request, e);

		return RestErrorMessage.create(e);
	}

}
