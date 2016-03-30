package hu.bankmonitor.starter.microservice.common.errorhandling;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Builder
@Data
@JsonInclude(Include.NON_NULL)
public class RestErrorMessage {

	private static final String BINDING_RESULT_KEY = "bindingResult";

	private final boolean success = false;

	private String type;

	private Map<String, Object> fieldErrors;

	public static RestErrorMessage create() {

		return RestErrorMessage.builder().type(ExceptionType.UNKNOWN_ERROR.toString()).build();
	}

	public static RestErrorMessage create(ExceptionWithExceptionContext exception) {

		RestErrorMessageBuilder restErrorMessageBuilder = RestErrorMessage.builder().type(exception.getExceptionContext().getType().name());

		if (exception.getExceptionContext().getData().containsKey(BINDING_RESULT_KEY)) {
			restErrorMessageBuilder.fieldErrors(getErrorMessages((BindingResult) exception.getExceptionContext().getData().get(BINDING_RESULT_KEY)));
		}

		return restErrorMessageBuilder.build();
	}

	private static Map<String, Object> getErrorMessages(BindingResult result) {

		Map<String, Object> errorMessages = new HashMap<>();
		for (FieldError error : result.getFieldErrors()) {
			errorMessages.put(error.getField(), error.getDefaultMessage());
		}

		return errorMessages;
	}

}
