package hu.bankmonitor.starter.microservice.common.web;

import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

@ConfigurationProperties("microservice-starters")
@Data
public class CorsMappingProperties {

	private Map<String, CorsConfiguration> corsMappings;

}
