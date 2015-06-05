package hu.bankmonitor.starter.microservice.common.log;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

import hu.bankmonitor.commons.logback.AwsLogsJsonAppender;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AwsLogsConfig {

	@Autowired(required = false)
	private AwsLogsProperties properties;

	@PostConstruct
	public void init() {

		if (properties != null) {
			log.info("Init AWS Logs appender with Log Stream: {}", properties.getLogStreamName());

			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

			AwsLogsJsonAppender awsLogsJsonAppender = new AwsLogsJsonAppender();
			awsLogsJsonAppender.setName("AWS_LOGS_APPENDER");
			awsLogsJsonAppender.setContext(loggerContext);

			awsLogsJsonAppender.setAwsAccessKey(properties.getAwsAccessKey());
			awsLogsJsonAppender.setAwsSecretKey(properties.getAwsSecretKey());
			awsLogsJsonAppender.setAwsRegionName(properties.getAwsRegionName());
			awsLogsJsonAppender.setLogGroupName(properties.getLogGroupName());
			awsLogsJsonAppender.setLogStreamName(properties.getLogStreamName());

			awsLogsJsonAppender.start();

			Logger rootLogger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
			rootLogger.addAppender(awsLogsJsonAppender);
		} else {
			log.debug("No AWS Logs config.");
		}
	}

}
