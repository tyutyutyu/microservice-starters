package hu.bankmonitor.starter.microservice.common.actuator;

import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

public class SystemDateTimeIndicator implements HealthIndicator {

	// @formatter:off
	private static final DateTimeFormatter PATTERN_FORMAT = new DateTimeFormatterBuilder()
	        .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
	        .appendTimeZoneOffset("Z", true, 2, 4)
	        .toFormatter();
	// @formatter:on

	@Override
	public Health health() {

		// @formatter:off
		return Health.up()
				.withDetail("systemDateTime", DateTime.now().toString(PATTERN_FORMAT))
				.withDetail("localDateTime", LocalDateTime.now().toString(PATTERN_FORMAT))
				.withDetail("timeZone", TimeZone.getDefault().getDisplayName())
				.build();
		// @formatter:on
	}

}
