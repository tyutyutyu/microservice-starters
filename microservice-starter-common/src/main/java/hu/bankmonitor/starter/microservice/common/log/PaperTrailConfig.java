package hu.bankmonitor.starter.microservice.common.log;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.tyutyutyu.logback.Syslog4jAppender;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.graylog2.syslog4j.impl.net.tcp.ssl.SSLTCPNetSyslogConfig;
import org.graylog2.syslog4j.util.SyslogUtility;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class PaperTrailConfig {

	private static final String CUSTOM_LOCAL_NAME_KEY = "CUSTOM_LOCAL_NAME";

	@Autowired(required = false)
	private PaperTrailProperties paperTrailProperties;

	/**
	 * Register the PaperTrail log appander if the appropriate properties exist.
	 */
	@PostConstruct
	void init() {

		if (paperTrailProperties != null) {
			log.info("Init Paper Trail appender with Ident: {}, Local name: {}", paperTrailProperties().getIdent());

			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

			Syslog4jAppender<ILoggingEvent> syslog4jAppender = new Syslog4jAppender<>();
			syslog4jAppender.setName("PAPER_TRAIL_SYSLOG4J_APPENDER");
			syslog4jAppender.setContext(loggerContext);
			PatternLayout layout = new PatternLayout();
			layout.setContext(loggerContext);
			layout.setPattern(paperTrailProperties.getPattern());
			layout.start();
			syslog4jAppender.setLayout(layout);

			SSLTCPNetSyslogConfig syslogConfig = new SSLTCPNetSyslogConfig();
			syslogConfig.setHost(paperTrailProperties.getHost());
			syslogConfig.setPort(paperTrailProperties.getPort());
			syslogConfig.setIdent(paperTrailProperties.getIdent());
			syslogConfig.setLocalName(System.getProperty(CUSTOM_LOCAL_NAME_KEY, SyslogUtility.getLocalName()));
			syslogConfig.setMaxMessageLength(paperTrailProperties.getMaxMessageLength());
			syslog4jAppender.setSyslogConfig(syslogConfig);

			syslog4jAppender.start();

			Logger rootLogger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
			rootLogger.addAppender(syslog4jAppender);
		} else {
			log.debug("No PaperTrail config.");
		}
	}

	@Bean
	@ConditionalOnProperty(prefix = "microservice-starters.paper-trail", name = { "host", "port" })
	@SuppressWarnings("static-method")
	PaperTrailProperties paperTrailProperties() {

		return new PaperTrailProperties();
	}

}
