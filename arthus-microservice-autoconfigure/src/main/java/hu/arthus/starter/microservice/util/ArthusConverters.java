package hu.arthus.starter.microservice.util;

import java.util.Date;

import org.joda.time.LocalDate;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * Wrapper osztály Spring-es konverterekhez.
 *
 * @see org.springframework.data.mongodb.core.convert.MongoConverters
 *
 * @author istvan.foldhazi
 *
 */
public final class ArthusConverters {

	private ArthusConverters() {

	}

	/**
	 * Simple singleton to convert {@link Date}s to their {@link LocalDate} representation.
	 *
	 * @author Oliver Gierke
	 */
	public static enum DateToLocalDateConverter implements Converter<Date, LocalDate> {
		INSTANCE;

		@Override
		public LocalDate convert(Date source) {

			return new LocalDate(source);
		}
	}

}
