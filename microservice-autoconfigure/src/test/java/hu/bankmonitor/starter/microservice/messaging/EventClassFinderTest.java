package hu.bankmonitor.starter.microservice.messaging;

import static org.assertj.core.api.Assertions.assertThat;

import hu.bankmonitor.starter.microservice.common.errorhandling.MicroserviceStarterRuntimeException;
import hu.bankmonitor.starter.microservice.messaging.workingconfig.EEvent;
import hu.bankmonitor.starter.microservice.messaging.workingconfig.FEvent;
import java.util.Set;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class EventClassFinderTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	@SuppressWarnings("static-method")
	public void testFindEventClassesWithWorkingConfig() {

		// given
		EventClassFinder eventClassFinder = new EventClassFinder("hu.bankmonitor.starter.microservice.messaging.workingconfig");

		// when
		Set<String> eventClasses = eventClassFinder.findEventClasses();

		// then
		assertThat(eventClasses).contains(EEvent.class.getName(), FEvent.class.getName());
	}

	@Test
	public void testFindEventClassesWithMoreHandlers() {

		// given
		EventClassFinder eventClassFinder = new EventClassFinder("hu.bankmonitor.starter.microservice.messaging.testmorehandlers");

		// then
		thrown.expect(MicroserviceStarterRuntimeException.class);

		// when
		eventClassFinder.findEventClasses();
	}

}
