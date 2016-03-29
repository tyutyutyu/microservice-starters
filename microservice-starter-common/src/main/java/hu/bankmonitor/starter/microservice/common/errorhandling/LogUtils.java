package hu.bankmonitor.starter.microservice.common.errorhandling;

import eu.bitwalker.useragentutils.BrowserType;
import eu.bitwalker.useragentutils.UserAgent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.MDC.MDCCloseable;

@Slf4j
public final class LogUtils {

	private static final String EOL = System.lineSeparator();

	private static final String SEPARATOR = new String(new char[100]).replace("\0", "=") + EOL;

	// @formatter:off
	private static final String LOG_MESSAGE =
			"{}" + EOL + EOL +
			SEPARATOR +
			"= Exception data" + EOL +
			SEPARATOR +
			"{}" +
			SEPARATOR +
			"= Request" + EOL +
			SEPARATOR +
			"{}" +
			SEPARATOR +
			"= Response" + EOL +
			SEPARATOR +
			"{}" +
			SEPARATOR;
	// @formatter:on

	private LogUtils() {

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

		StringBuilder sb = new StringBuilder();
		sb.append("URI        :").append(request.getRequestURI()).append(EOL);
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

	public static void logException(Throwable exception, HttpServletRequest request) {

		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		if (userAgent.getBrowser().getBrowserType() == BrowserType.ROBOT) {
			log.trace(exception.getMessage(), exception);
		} else {
			log.error(exception.getMessage(), exception);
		}
	}

	public static void logException(ExceptionWithExceptionData exception, HttpServletRequest request, RestErrorMessage response) {

		Logger exceptionLogger = LoggerFactory.getLogger(exception.getExceptionData().getType().toString());

		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));

		try (
			// @formatter:off
				MDCCloseable exceptioDataTypeCloseable = MDC.putCloseable("exceptionData.type", exception.getExceptionData().getType().toString());
				MDCCloseable exceptioDataMessageCloseable = MDC.putCloseable("exceptionData.message", exception.getExceptionData().getMessage());
				MDCCloseable exceptioDataDataCloseable = MDC.putCloseable("exceptionData.data", exception.getExceptionData().getData().toString());
			// @formatter:on
		) {

			if (userAgent.getBrowser().getBrowserType() == BrowserType.ROBOT) {
				// log.trace("{} - \n===================================================\n= request\n===================================================\n{}", method, log(request),
				// e);
			} else {
				exceptionLogger.error(LOG_MESSAGE, exception.getExceptionData().getType().toString(), log(exception.getExceptionData()), log(request), log(response), exception);
			}
		}
	}

	private static Object log(ExceptionData exceptionData) {

		return exceptionData.toString() + EOL;
	}

	private static String log(RestErrorMessage response) {

		return response.toString() + EOL;
	}

}
