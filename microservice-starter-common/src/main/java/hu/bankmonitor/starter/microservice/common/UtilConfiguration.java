package hu.bankmonitor.starter.microservice.common;

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
	 * Register {@link Converter}-s for {@link ConfigurationProperties} classes in {@link ConversionService}.
	 *
	 * @return A ConversionService
	 */
	@Bean(name = ConfigurableApplicationContext.CONVERSION_SERVICE_BEAN_NAME)
	@SuppressWarnings("static-method")
	public ConversionService conversionService() {

		GenericConversionService conversionService = new DefaultConversionService();
		conversionService.addConverter(MicroserviceStarterConverters.DateToLocalDateConverter.INSTANCE);

		return conversionService;
	}

}
