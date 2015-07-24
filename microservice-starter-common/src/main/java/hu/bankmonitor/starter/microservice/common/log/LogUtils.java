package hu.bankmonitor.starter.microservice.common.log;

import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class LogUtils {

	public static String log(HttpServletRequest request) {

		StringBuffer sb = new StringBuffer();
		sb.append("\nURI        :").append(request.getRequestURI());
		Collections.list(request.getHeaderNames()).stream().forEach(n -> {
			sb.append("\nHeader     :").append(n).append("=").append(request.getHeader(n));
		});
		request.getParameterMap().entrySet().stream().forEach(e -> {
			sb.append("\nParam      :").append(e.getKey()).append("=").append(Arrays.toString(e.getValue()));
		});

		return sb.toString();

	}

}
