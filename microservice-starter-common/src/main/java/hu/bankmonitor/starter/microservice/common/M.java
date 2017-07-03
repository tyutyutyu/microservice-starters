package hu.bankmonitor.starter.microservice.common;

import org.slf4j.Marker;
import org.slf4j.helpers.BasicMarkerFactory;

public final class M {

	/**
	 * Marker for method parameter logging
	 */
	public static final Marker PARAMS = new BasicMarkerFactory().getMarker("PARAMS");

	/**
	 * Marker for method return value logging
	 */
	public static final Marker RETURN = new BasicMarkerFactory().getMarker("RETURN");

	private M() {

	}

}
