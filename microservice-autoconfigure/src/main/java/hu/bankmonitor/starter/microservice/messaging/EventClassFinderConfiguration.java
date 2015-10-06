package hu.bankmonitor.starter.microservice.messaging;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class EventClassFinderConfiguration {

	private static final String BASE_PACKAGE = "hu";

	public static interface EventClassFinder {

		Set<String> findEventClasses();

	}

	@ConditionalOnMissingClass(name = "org.axonframework.eventhandling.annotation.EventHandler")
	@Configuration
	public static class RabbitEventClassFinderConfiguration {

		@Bean
		@SuppressWarnings("static-method")
		public EventClassFinder eventClassFinder() {

			return new RabbitEventClassFinder();
		}

		public static class RabbitEventClassFinder implements EventClassFinder {

			private static final String MESSAGE_HANDLER_METHOD_NAME = "handleMessage";

			@Override
			@SuppressWarnings("synthetic-access")
			public Set<String> findEventClasses() {

				return findParameterTypeOfMethodsWithName(MESSAGE_HANDLER_METHOD_NAME);
			}
		}
	}

	private static Set<String> findParameterTypeOfMethodsWithName(String methodName) {

		ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(false);
		componentProvider.addIncludeFilter(new AssignableTypeFilter(AbstractMessageHandler.class));

		Set<String> eventClasses = new HashSet<>();
		for (BeanDefinition bd : componentProvider.findCandidateComponents(BASE_PACKAGE)) {

			try {
				Method[] allDeclaredMethods =
						ReflectionUtils.getAllDeclaredMethods(ClassUtils.forName(bd.getBeanClassName(), EventClassFinderConfiguration.class.getClassLoader()));

				for (Method method : allDeclaredMethods) {
					if (methodName.equals(method.getName())) {

						assert method.getParameterCount() != 1;

						String eventClass = method.getParameters()[0].getType().getName();
						log.debug("Handled event class: {}", eventClass);
						eventClasses.add(eventClass);
					}
				}

			} catch (IllegalArgumentException | ClassNotFoundException | LinkageError e) {
				// TODO: FI: review exception handling
				log.error("Class load error: {}", e.getMessage());
				log.error("Exception:", e);
			}
		}

		return eventClasses;
	}

}
