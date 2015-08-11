package hu.bankmonitor.starter.microservice.common.log;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class LogUtils {

	public static String log(HttpServletRequest request) {

		StringBuffer sb = new StringBuffer();
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

}
