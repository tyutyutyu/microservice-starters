package hu.bankmonitor.starter.microservice.common.errorhandling;

import java.io.Serializable;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class ExceptionData implements Serializable {

	private static final long serialVersionUID = 1L;

	@NonNull
	private final Enum<?> type;

	private Throwable cause;

	private transient Map<String, Object> data;

	private transient HttpServletRequest request;

}
