package hu.bankmonitor.starter.microservice.common.log;

import org.productivity.java.syslog4j.SyslogMessageProcessorIF;
import org.productivity.java.syslog4j.impl.net.tcp.ssl.SSLTCPNetSyslog;

public class BankmonitorSSLTCPNetSyslog extends SSLTCPNetSyslog {

	private static final long serialVersionUID = 1L;

	@Override
	public SyslogMessageProcessorIF getMessageProcessor() {

		if (syslogMessageProcessor == null) {
			syslogMessageProcessor = BankmonitorSyslogMessageProcessor.INSTANCE;
		}

		return syslogMessageProcessor;
	}

}
