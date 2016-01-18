package hu.bankmonitor.starter.microservice.common.errorhandling;

import java.util.Map;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.MDC;

@UtilityClass
public final class ExceptionUtils {

	public static void logError(Logger logger, String contextMessage, BankmonitorException exception) {

		MDC.put("exceptionType", exception.getType().toString());
		MDC.put("exceptionId", exception.getId());
		MDC.put("exceptionContextMessage", contextMessage);

		for (Map.Entry<String, Object> entry : exception.getData().entrySet()) {
			MDC.put("exceptionContextData-" + entry.getKey(), entry.getValue().toString());
		}

		logger.error(exception.getType().toString(), exception);

		MDC.clear();
	}

}
