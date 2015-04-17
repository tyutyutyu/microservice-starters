package hu.bankmonitor.starter.microservice.common;

import org.slf4j.Marker;
import org.slf4j.helpers.BasicMarkerFactory;

public final class M {

	private M() {

	}

	public static final Marker PARAMS = new BasicMarkerFactory().getMarker("PARAMS");

	public static final Marker RETURN = new BasicMarkerFactory().getMarker("RETURN");

}
