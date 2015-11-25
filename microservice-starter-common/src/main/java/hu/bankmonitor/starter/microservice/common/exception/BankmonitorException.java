package hu.bankmonitor.starter.microservice.common.exception;

import java.util.Map;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true, of = { })
public class BankmonitorException extends Exception {

	private static final long serialVersionUID = 1L;

	private Enum<?> type;

	private String id = UUID.randomUUID().toString();

	private Map<String, Object> data;

	public BankmonitorException(Enum<?> type, Exception exception, Map<String, Object> data) {
		super(exception);
		this.type = type;
		this.data = data;
	}

}
