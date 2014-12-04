package hu.arthus.starter.microservice.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

@Configuration
public class UtilConfiguration {

	/**
	 * A {@link ConfigurationProperties} osztályoknál szükséges ez a {@link ConversionService} az extra {@link Converter}-ekhez.
	 *
	 * @return A ConversionService
	 */
	@Bean(name = ConfigurableApplicationContext.CONVERSION_SERVICE_BEAN_NAME)
	@SuppressWarnings("static-method")
	public ConversionService conversionService() {

		GenericConversionService conversionService = new DefaultConversionService();
		conversionService.addConverter(ArthusConverters.DateToLocalDateConverter.INSTANCE);

		return conversionService;
	}

}
