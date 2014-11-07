package hu.arthus.starter.microservice.adminclient;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;

@ConditionalOnClass(EnableEurekaClient.class)
@Configuration
@EnableEurekaClient
@EnableConfigurationProperties(EurekaClientProperties.class)
@Slf4j
public class AdminClientConfiguration {

	@Autowired
	private EurekaClientProperties properties;

	@PostConstruct
	void init() {

		log.debug("AdminClientConfiguration loaded. [{}]", properties);
	}

}
