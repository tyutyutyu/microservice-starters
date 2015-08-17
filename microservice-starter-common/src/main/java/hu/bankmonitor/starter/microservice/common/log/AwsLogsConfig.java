package hu.bankmonitor.starter.microservice.common.log;

import hu.bankmonitor.commons.logback.AwsLogsJsonAppender;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AwsLogsConfig {

	@Autowired(required = false)
	private AwsLogsProperties properties;

	@PostConstruct
	void init() {

		if (properties != null) {
			log.info("Init AWS Logs appender with Log Stream: {}", properties().getLogStreamName());

			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

			AwsLogsJsonAppender awsLogsJsonAppender = new AwsLogsJsonAppender();
			awsLogsJsonAppender.setName("AWS_LOGS_APPENDER");
			awsLogsJsonAppender.setContext(loggerContext);

			awsLogsJsonAppender.setAwsAccessKey(properties().getAwsAccessKey());
			awsLogsJsonAppender.setAwsSecretKey(properties().getAwsSecretKey());
			awsLogsJsonAppender.setAwsRegionName(properties().getAwsRegionName());
			awsLogsJsonAppender.setLogGroupName(properties().getLogGroupName());
			awsLogsJsonAppender.setLogStreamName(properties().getLogStreamName());

			awsLogsJsonAppender.start();

			Logger rootLogger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
			rootLogger.addAppender(awsLogsJsonAppender);
		} else {
			log.debug("No AWS Logs config.");
		}
	}

	@Bean
	@ConditionalOnProperty(prefix = "application.awslogs", name = { "awsAccessKey", "awsSecretKey" })
	@SuppressWarnings("static-method")
	AwsLogsProperties properties() {

		return new AwsLogsProperties();
	}

}
