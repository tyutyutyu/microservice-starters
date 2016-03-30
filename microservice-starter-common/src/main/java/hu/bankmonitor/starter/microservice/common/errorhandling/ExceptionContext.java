package hu.bankmonitor.starter.microservice.common.errorhandling;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@Getter
@ToString
class ExceptionContext implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String id = UUID.randomUUID().toString();

	@NonNull
	private final Enum<?> type;

	private Map<String, Object> data;

	@Setter(AccessLevel.PACKAGE)
	private HttpServletRequest request;

	public static ExceptionContext create(ExceptionData exceptionData) {

		// @formatter:off
		return ExceptionContext.builder()
				.type(exceptionData.getType())
				.data(exceptionData.getData())
				.request(exceptionData.getRequest())
				.build();
		// @formatter:on
	}

}
