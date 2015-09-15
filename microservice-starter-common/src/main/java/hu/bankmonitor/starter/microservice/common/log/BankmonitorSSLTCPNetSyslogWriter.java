package hu.bankmonitor.starter.microservice.common.log;

import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.impl.net.tcp.ssl.SSLTCPNetSyslogWriter;

public class BankmonitorSSLTCPNetSyslogWriter extends SSLTCPNetSyslogWriter {

	private static final long serialVersionUID = 1L;

	@Override
	public void write(byte[] message) throws SyslogRuntimeException {

		System.err.println("BankmonitorSSLTCPNetSyslogWriter.write - message: " + new String(message));

		super.write(message);
	}

}
