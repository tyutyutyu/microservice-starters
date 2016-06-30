package hu.bankmonitor.starter.microservice.common.web;

import com.google.common.base.MoreObjects;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@Slf4j
public class WebConfiguration extends WebMvcConfigurerAdapter {

	@Autowired(required = false)
	private CorsMappingProperties properties;

	@PostConstruct
	public void init() {

		log.debug("init - corsMappings: {}", properties);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {

		if (properties != null && properties.getCorsMappings() != null) {
			for (Map.Entry<String, CorsConfiguration> mappingEntry : properties.getCorsMappings().entrySet()) {
				// @formatter:off
				registry
						.addMapping(mappingEntry.getKey())
							.allowedOrigins(extractCorsProps(mappingEntry.getValue().getAllowedOrigins()))
							.allowedMethods(extractCorsProps(mappingEntry.getValue().getAllowedMethods()))
							.allowedHeaders(extractCorsProps(mappingEntry.getValue().getAllowedHeaders()))
							.exposedHeaders(extractCorsProps(mappingEntry.getValue().getExposedHeaders()))
							.allowCredentials(BooleanUtils.isFalse(mappingEntry.getValue().getAllowCredentials()))
							.maxAge(MoreObjects.firstNonNull(mappingEntry.getValue().getMaxAge(), CrossOrigin.DEFAULT_MAX_AGE));
				// @formatter:on
			}
		}
	}

	private static String[] extractCorsProps(List<String> prop) {

		if (CollectionUtils.isEmpty(prop)) {
			return new String[] { };
		}

		return prop.toArray(new String[] { });
	}

}
