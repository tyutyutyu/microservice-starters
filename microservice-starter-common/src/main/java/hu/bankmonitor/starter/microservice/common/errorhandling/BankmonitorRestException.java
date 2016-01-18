package hu.bankmonitor.starter.microservice.common.errorhandling;

import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true, of = { })
public class BankmonitorRestException extends BankmonitorException {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("ucd")
	public BankmonitorRestException(Enum<?> exType, Map<String, Object> data) {
		super(exType, data);
	}

	@SuppressWarnings("ucd")
	public BankmonitorRestException(Enum<?> exType) {
		super(exType);
	}

	@SuppressWarnings("ucd")
	public BankmonitorRestException(Throwable cause, Enum<?> exType, Map<String, Object> data) {
		super(cause, exType, data);
	}

	@SuppressWarnings("ucd")
	public BankmonitorRestException(Throwable cause, Enum<?> exType) {
		super(cause, exType);
	}

}
