package hu.bankmonitor.starter.microservice.common;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;

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
			logentriesAppender.setContext(loggerContext);
			logentriesAppender.setDebug(false);
			logentriesAppender.setToken(token);
			logentriesAppender.setSsl(false);
			logentriesAppender.setFacility("USER");
			PatternLayout patternLayout = new PatternLayout();
			patternLayout.setContext(loggerContext);
			patternLayout.setPattern("%d{HH:mm:ss.SSS} [%thread] [%-5level] %logger{50} - %msg%n");
			logentriesAppender.setLayout(patternLayout);

			logentriesAppender.start();

			Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
			root.addAppender(logentriesAppender);
		}
	}

}
