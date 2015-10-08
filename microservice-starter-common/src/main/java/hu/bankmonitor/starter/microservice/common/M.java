package hu.bankmonitor.starter.microservice.common;

import lombok.experimental.UtilityClass;
import org.slf4j.Marker;
import org.slf4j.helpers.BasicMarkerFactory;

@UtilityClass
public final class M {

	public static final Marker PARAMS = new BasicMarkerFactory().getMarker("PARAMS");

	public static final Marker RETURN = new BasicMarkerFactory().getMarker("RETURN");

}
