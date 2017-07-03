package hu.bankmonitor.starter.microservice.common.log;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.filter.ThresholdFilter;
import io.sentry.Sentry;
import io.sentry.logback.SentryAppender;
import io.sentry.servlet.SentryServletContainerInitializer;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@Slf4j
public class SentryConfig {

	private static final String[] MUST_BE_REGISTERED_PROFILES = new String[] { "test", "live" };

	@Autowired
	private Environment env;

	@Autowired(required = false)
	private SentryProperties sentryProperties;

	/**
	 * Register the Sentry log appander if the appropriate properties exist.
	 */
	@PostConstruct
	public void init() {

		if (sentryProperties != null) {

			log.info("Init Sentry appender with dsn: {}", sentryProperties.getDsn());

			Sentry.init(sentryProperties.getDsn());

			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

			SentryAppender sentryAppender = new SentryAppender();
			sentryAppender.setName("SENTRY_APPENDER");
			sentryAppender.setContext(loggerContext);

			ThresholdFilter thresholdFilter = new ThresholdFilter();
			thresholdFilter.setLevel(sentryProperties.getLogLevel());
			thresholdFilter.start();
			sentryAppender.addFilter(thresholdFilter);

			sentryAppender.start();

			Logger rootLogger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
			rootLogger.addAppender(sentryAppender);
		} else {
			if (env.acceptsProfiles(MUST_BE_REGISTERED_PROFILES)) {
				log.error("No Sentry dsn!");
			} else {
				log.debug("No Sentry dsn.");
			}
		}
	}

	@Bean
	@ConditionalOnProperty(prefix = "microservice-starters.sentry", name = { "dsn" })
	@SuppressWarnings("static-method")
	SentryProperties sentryProperties() {

		return new SentryProperties();
	}

	/**
	 * RavenServletContainerInitializer fix. (See:
	 * http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#howto-add-a-servlet-filter-or-servletcontextlistener)
	 *
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(prefix = "microservice-starters.sentry", name = { "dsn" })
	@SuppressWarnings("static-method")
	public ServletContextInitializer servletContextInitializer() {

		return new ServletContextInitializer() {

			private SentryServletContainerInitializer sentryServletContainerInitializer = new SentryServletContainerInitializer();

			@Override
			public void onStartup(ServletContext servletContext) throws ServletException {

				sentryServletContainerInitializer.onStartup(null, servletContext);
			}
		};
	}

}
