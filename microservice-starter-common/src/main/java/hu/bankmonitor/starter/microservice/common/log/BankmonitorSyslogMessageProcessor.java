package hu.bankmonitor.starter.microservice.common.log;

import org.productivity.java.syslog4j.impl.message.processor.SyslogMessageProcessor;
import org.productivity.java.syslog4j.util.SyslogUtility;

public class BankmonitorSyslogMessageProcessor extends SyslogMessageProcessor {

	protected static final BankmonitorSyslogMessageProcessor INSTANCE = new BankmonitorSyslogMessageProcessor();

	private static final long serialVersionUID = 1L;

	private static final String CUSTOM_LOCAL_NAME_KEY = "CUSTOM_LOCAL_NAME";

	/**
	 * Override the original local name with the CUSTOM_LOCAL_NAME system property if it is exists.
	 */
	@Override
	public String createSyslogHeader(int facility, int level, boolean sendLocalTimestamp, boolean sendLocalName) {

		String syslogHeader = super.createSyslogHeader(facility, level, sendLocalTimestamp, sendLocalName);
		if (sendLocalName) {
			syslogHeader = syslogHeader.replace(SyslogUtility.getLocalName(), getLocalName());
		}

		return syslogHeader;
	}

	public static String getLocalName() {

		return System.getProperty(CUSTOM_LOCAL_NAME_KEY, SyslogUtility.getLocalName());
	}

}
