package hu.bankmonitor.starter.microservice.common.log;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.filter.ThresholdFilter;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.kencochrane.raven.logback.SentryAppender;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

@Configuration
@Slf4j
public class SentryConfig {

	private static final String[] MUST_BE_REGISTERED_PROFILES = new String[] { "test", "live" };

	@Autowired
	private Environment env;

	@Value("${application.sentryDsn:}")
	private String dsn;

	/**
	 * Register the Sentry log appander if the appropriate properties exist.
	 */
	@PostConstruct
	public void init() {

		if (!StringUtils.isEmpty(dsn)) {

			log.info("Init Sentry appender with dsn: {}", dsn);

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
		} else {
			if (env.acceptsProfiles(MUST_BE_REGISTERED_PROFILES)) {
				log.error("No Sentry dsn!");
			} else {
				log.debug("No Sentry dsn.");
			}
		}
	}

}
