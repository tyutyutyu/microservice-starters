package hu.bankmonitor.starter.microservice.common;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

@Configuration
public class UtilConfiguration {

	@Bean(name = ConfigurableApplicationContext.CONVERSION_SERVICE_BEAN_NAME)
	@SuppressWarnings("static-method")
	public ConversionService conversionService() {

		GenericConversionService conversionService = new DefaultConversionService();
		conversionService.addConverter(MicroserviceStarterConverters.DateToLocalDateConverter.INSTANCE);

		return conversionService;
	}

}
