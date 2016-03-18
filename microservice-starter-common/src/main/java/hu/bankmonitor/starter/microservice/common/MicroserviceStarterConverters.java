package hu.bankmonitor.starter.microservice.common;

import java.util.Date;
import org.joda.time.LocalDate;
import org.springframework.core.convert.converter.Converter;

/**
 * Wrapper class for Spring converters
 *
 * @author istvan.foldhazi
 *
 */
public final class MicroserviceStarterConverters {

	private MicroserviceStarterConverters() {

	}

	/**
	 * Simple singleton to convert {@link Date}s to their {@link LocalDate} representation.
	 *
	 * @author Oliver Gierke
	 */
	public enum DateToLocalDateConverter implements Converter<Date, LocalDate> {

		INSTANCE;

		@Override
		public LocalDate convert(Date source) {

			return new LocalDate(source);
		}
	}

}
