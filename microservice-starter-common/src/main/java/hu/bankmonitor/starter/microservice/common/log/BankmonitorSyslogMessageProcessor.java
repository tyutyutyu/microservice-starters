package hu.bankmonitor.starter.microservice.common.log;

import org.productivity.java.syslog4j.impl.message.processor.SyslogMessageProcessor;
import org.productivity.java.syslog4j.util.SyslogUtility;

public class BankmonitorSyslogMessageProcessor extends SyslogMessageProcessor {

	protected static final BankmonitorSyslogMessageProcessor INSTANCE = new BankmonitorSyslogMessageProcessor();

	private static final long serialVersionUID = 1L;

	private static final String CUSTOM_LOCAL_NAME_KEY = "CUSTOM_LOCAL_NAME";

	private static final String DEFAULT_CUSTOM_NAME = "unknown";

	@Override
	public String createSyslogHeader(int facility, int level, boolean sendLocalTimestamp, boolean sendLocalName) {

		String syslogHeader = super.createSyslogHeader(facility, level, sendLocalTimestamp, sendLocalName);
		if (sendLocalName) {
			syslogHeader = syslogHeader.replace(SyslogUtility.getLocalName(), System.getProperty(CUSTOM_LOCAL_NAME_KEY, DEFAULT_CUSTOM_NAME));
		}

		return syslogHeader;
	}

}
