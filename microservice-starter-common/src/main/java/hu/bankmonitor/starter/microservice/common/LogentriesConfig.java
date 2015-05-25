package hu.bankmonitor.starter.microservice.common;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;

import com.logentries.logback.LogentriesAppender;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LogentriesConfig {

	@Value("${application.logEntriesToken:null}")
	private String token;

	@PostConstruct
	public void init() {

		log.info("Init Logentries appender with token: {}", token);

		if (token != null) {

			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

			LogentriesAppender logentriesAppender = new LogentriesAppender();
			logentriesAppender.setName("LOGENTRIES_APPENDER");
			logentriesAppender.setContext(loggerContext);
			logentriesAppender.setDebug(true);
			logentriesAppender.setToken("f26fc026-4769-45b4-b747-4ecad9cbb907");
			logentriesAppender.setSsl(false);
			logentriesAppender.setFacility("USER");

			PatternLayoutEncoder patternLayoutEncoder = new PatternLayoutEncoder();
			patternLayoutEncoder.setContext(loggerContext);
			patternLayoutEncoder.setPattern("%d{HH:mm:ss.SSS} [%thread] [%-5level] %logger{50} - %msg%n");
			patternLayoutEncoder.start();
			logentriesAppender.setEncoder(patternLayoutEncoder);

			logentriesAppender.start();

			Logger rootLogger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
			rootLogger.addAppender(logentriesAppender);
		}
	}

}
