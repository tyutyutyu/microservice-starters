package hu.bankmonitor.starter.microservice.common.log;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.filter.ThresholdFilter;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import net.kencochrane.raven.logback.SentryAppender;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SentryConfig {

	@Value("${application.sentryDsn:null}")
	private String dsn;

	@PostConstruct
	public void init() {

		log.info("Init Sentry appender with dsn: {}", dsn);

		if (dsn != null) {

			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

			SentryAppender sentryAppender = new SentryAppender();
			sentryAppender.setName("SENTRY_APPENDER");
			sentryAppender.setContext(loggerContext);
			sentryAppender.setDsn(dsn);

			ThresholdFilter thresholdFilter = new ThresholdFilter();
			thresholdFilter.setLevel("ERROR");
			thresholdFilter.start();
			sentryAppender.addFilter(thresholdFilter);

			sentryAppender.start();

			Logger rootLogger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
			rootLogger.addAppender(sentryAppender);
		}
	}

}
