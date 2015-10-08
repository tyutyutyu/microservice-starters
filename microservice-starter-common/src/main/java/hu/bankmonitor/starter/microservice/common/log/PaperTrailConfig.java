package hu.bankmonitor.starter.microservice.common.log;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.papertrailapp.logback.Syslog4jAppender;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class PaperTrailConfig {

	@Autowired(required = false)
	private PaperTrailProperties properties;

	/**
	 * Register the PaperTrail log appander if the appropriate properties exist.
	 */
	@PostConstruct
	void init() {

		if (properties != null) {
			log.info("Init Paper Trail appender with Ident: {}, Local name: {}", properties().getIdent(), BankmonitorSyslogMessageProcessor.getLocalName());

			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

			Syslog4jAppender<ILoggingEvent> syslog4jAppender = new Syslog4jAppender<>();
			syslog4jAppender.setName("PAPER_TRAIL_SYSLOG4J_APPENDER");
			syslog4jAppender.setContext(loggerContext);
			PatternLayout layout = new PatternLayout();
			layout.setContext(loggerContext);
			layout.setPattern(properties.getPattern());
			layout.start();
			syslog4jAppender.setLayout(layout);

			BankmonitorSSLTCPNetSyslogConfig syslogConfig = new BankmonitorSSLTCPNetSyslogConfig();
			syslogConfig.setHost(properties.getHost());
			syslogConfig.setPort(properties.getPort());
			syslogConfig.setIdent(properties.getIdent());
			syslogConfig.setMaxMessageLength(properties.getMaxMessageLength());
			syslog4jAppender.setSyslogConfig(syslogConfig);

			syslog4jAppender.start();

			Logger rootLogger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
			rootLogger.addAppender(syslog4jAppender);
		} else {
			log.debug("No PaperTrail config.");
		}
	}

	@Bean
	@ConditionalOnProperty(prefix = "application.paper-trail", name = { "host", "port" })
	@SuppressWarnings("static-method")
	PaperTrailProperties properties() {

		return new PaperTrailProperties();
	}

}
