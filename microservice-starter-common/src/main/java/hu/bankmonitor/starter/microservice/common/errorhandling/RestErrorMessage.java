package hu.bankmonitor.starter.microservice.common.errorhandling;

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Builder
@Data
public class RestErrorMessage {

	private String type;

	private Map<String, Object> error;

	public static RestErrorMessage create(BankmonitorException bankmonitorException) {

		return RestErrorMessage.builder().type(bankmonitorException.getType().name()).error(getErrorMessages((BindingResult) bankmonitorException.getData().get("result"))).build();
	}

	private static Map<String, Object> getErrorMessages(BindingResult result) {

		Map<String, Object> errorMessages = new HashMap<>();
		for (FieldError error : result.getFieldErrors()) {
			errorMessages.put(error.getField(), error.getDefaultMessage());
		}
		return errorMessages;
	}

}
