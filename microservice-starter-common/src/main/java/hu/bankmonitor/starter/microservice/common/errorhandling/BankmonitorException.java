package hu.bankmonitor.starter.microservice.common.errorhandling;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true, of = { })
public class BankmonitorException extends Exception {

	private static final long serialVersionUID = 1L;

	private final Enum<?> type;

	private final String id = UUID.randomUUID().toString();

	private final Map<String, Object> data;

	@SuppressWarnings("ucd")
	public BankmonitorException(Enum<?> exType) {

		this((Throwable) null, exType);
	}

	@SuppressWarnings("ucd")
	public BankmonitorException(Enum<?> exType, Map<String, Object> data) {

		this(null, exType, data);
	}

	@SuppressWarnings("ucd")
	public BankmonitorException(Throwable cause, Enum<?> exType) {

		this(cause, exType, (Map<String, Object>) null);
	}

	@SuppressWarnings("ucd")
	public BankmonitorException(Throwable cause, Enum<?> exType, Map<String, Object> data) {

		super(cause);
		type = exType;
		this.data = ImmutableMap.copyOf(data);
	}

	@Override
	public String getMessage() {

		return super.getMessage() + " || exType: " + type + " || id: " + id + " || data: " + data;
	}

}
