package hu.bankmonitor.starter.microservice.common.log;

import eu.bitwalker.useragentutils.BrowserType;
import eu.bitwalker.useragentutils.UserAgent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;

public final class LogUtils {

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
		sb.append("\nURI        :").append(request.getRequestURI());
		for (Object n : Collections.list(request.getHeaderNames())) {
			sb.append("\nHeader     :").append(n).append("=").append(request.getHeader(n.toString()));
		}

		for (Object e : request.getParameterMap().entrySet()) {
			Map.Entry<?, ?> entry = (Map.Entry<?, ?>) e;
			if (entry.getValue() instanceof String[]) {
				sb.append("\nParam      :").append(entry.getKey()).append("=").append(Arrays.toString((String[]) entry.getValue()));
			} else if (entry.getValue() instanceof String) {
				sb.append("\nParam      :").append(entry.getKey()).append("=").append((String) entry.getValue());
			}
		}

		return sb.toString();
	}

	public static void logException(Logger log, String method, HttpServletRequest request, Exception e) {

		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));

		if (userAgent.getBrowser().getBrowserType() == BrowserType.ROBOT) {
			log.trace("{} - request: {}", method, LogUtils.log(request), e);
		} else {
			log.error("{} - request: {}", method, LogUtils.log(request), e);
		}
	}

}
