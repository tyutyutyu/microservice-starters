package hu.bankmonitor.starter.microservice.messaging;

import static org.assertj.core.api.Assertions.assertThat;

import hu.bankmonitor.starter.microservice.common.exception.MicroserviceStarterRuntimeException;
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
		thrown.expectMessage("Message handlers handling event types that do not inherit from AbstractEvent");

		// when
		eventClassFinder.findEventClasses();
	}

	@Test
	public void testFindEventClassesWithMoreEvents() {

		// given
		EventClassFinder eventClassFinder = new EventClassFinder("hu.bankmonitor.starter.microservice.messaging.testmoreevents");

		// then
		thrown.expect(MicroserviceStarterRuntimeException.class);
		thrown.expectMessage("Message handlers are not handling all event types");

		// when
		eventClassFinder.findEventClasses();
	}

}
