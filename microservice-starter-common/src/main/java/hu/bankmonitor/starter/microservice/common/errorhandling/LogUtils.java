package hu.bankmonitor.starter.microservice.common.errorhandling;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public final class LogUtils {

	private static final String EXCEPTION_CONTEXT_TYPE_KEY = "exceptionContext.type";

	private static final String EXCEPTION_CONTEXT_DATA_KEY = "exceptionContext.data";

	private static final String EOL = System.lineSeparator();

	private static final String SEPARATOR = new String(new char[100]).replace("\0", "=") + EOL;

	// @formatter:off
	private static final String LOG_MESSAGE =
			"{}" + EOL + EOL +
			SEPARATOR +
			"= Exception context" + EOL +
			SEPARATOR +
			"{}" +
			SEPARATOR +
			"= Request" + EOL +
			SEPARATOR +
			"{}" +
			SEPARATOR;
	// @formatter:on

	private LogUtils() {

	}

	static void logException(Throwable exception, HttpServletRequest request) {

		ExceptionContext exceptionContext;
		if (exception instanceof ExceptionWithExceptionContext) {
			exceptionContext = ((ExceptionWithExceptionContext) exception).getExceptionContext();
			exceptionContext.setRequest(request);
		} else {
			exceptionContext = ExceptionContext.builder().type(ExceptionType.UNKNOWN_ERROR).request(request).build();
		}

		logException(exception, exceptionContext);
	}

	public static void logException(ExceptionData exceptionData) {

		logException(exceptionData.getCause(), ExceptionContext.create(exceptionData));
	}

	static void logException(Throwable exception, ExceptionContext exceptionContext) {

		Logger exceptionLogger = LoggerFactory.getLogger(exceptionContext.getType().toString());

		MDC.put(EXCEPTION_CONTEXT_TYPE_KEY, exceptionContext.getType().toString());
		if (exceptionContext.getData() != null) {
			MDC.put(EXCEPTION_CONTEXT_DATA_KEY, exceptionContext.getData().toString());
		} else {
			MDC.put(EXCEPTION_CONTEXT_DATA_KEY, "NO_DATA");
		}

		exceptionLogger.error(LOG_MESSAGE, exceptionContext.getType().toString(), log(exceptionContext), log(exceptionContext.getRequest()), exception);

		MDC.remove(EXCEPTION_CONTEXT_TYPE_KEY);
		MDC.remove(EXCEPTION_CONTEXT_DATA_KEY);
	}

	private static Object log(ExceptionContext exceptionContext) {

		return exceptionContext.toString() + EOL;
	}

	/**
	 * Create developer friendly log message from the {@link HttpServletRequest}.
	 *
	 * @param request
	 *            The request to convert
	 *
	 * @return The message
	 */
	public static String log(HttpServletRequest request) {

		if (request == null) {
			return "NO REQUEST DATA" + EOL;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("URI        :").append(request.getRequestURI()).append(EOL);
		sb.append("Method     :").append(request.getMethod()).append(EOL);
		for (Object n : Collections.list(request.getHeaderNames())) {
			sb.append("Header     :").append(n).append("=").append(request.getHeader(n.toString())).append(EOL);
		}

		for (Object e : request.getParameterMap().entrySet()) {
			Map.Entry<?, ?> entry = (Map.Entry<?, ?>) e;
			if (entry.getValue() instanceof String[]) {
				sb.append("Param      :").append(entry.getKey()).append("=").append(Arrays.toString((String[]) entry.getValue()));
			} else if (entry.getValue() instanceof String) {
				sb.append("Param      :").append(entry.getKey()).append("=").append((String) entry.getValue());
			}
			sb.append(EOL);
		}

		return sb.toString();
	}

}
