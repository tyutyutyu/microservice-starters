package hu.bankmonitor.starter.microservice.common.log;

import org.productivity.java.syslog4j.impl.net.tcp.ssl.SSLTCPNetSyslogConfig;

import lombok.Setter;

@Setter
public class BankmonitorSSLTCPNetSyslogConfig extends SSLTCPNetSyslogConfig {

	private static final long serialVersionUID = 1L;

	@Override
	public Class<?> getSyslogClass() {

		return BankmonitorSSLTCPNetSyslog.class;
	}

	@Override
	public Class<?> getSyslogWriterClass() {

		return BankmonitorSSLTCPNetSyslogWriter.class;
	}

}
