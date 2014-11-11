package hu.arthus.starter.microservice.messaging;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

@Configuration
@Slf4j
public class EventClassFinderConfiguration {

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

		// FIXME: FI: finomítani az implementációt
		public static class RabbitEventClassFinder implements EventClassFinder {

			@Override
			@SuppressWarnings("synthetic-access")
			public Set<String> findEventClasses() {

				ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(false);
				componentProvider.addIncludeFilter(new AssignableTypeFilter(Object.class));

				Set<String> eventClasses = new HashSet<>();
				for (BeanDefinition bd : componentProvider.findCandidateComponents("hu.arthus")) {

					log.trace("Found class: {}", bd);

					if (bd.getBeanClassName().endsWith("Event")) {
						log.debug("Found event class: {}", bd);
						eventClasses.add(bd.getBeanClassName());
					}
				}

				return eventClasses;
			}
		}
	}

	@ConditionalOnClass(value = EventHandler.class)
	@Configuration
	public static class AxonEventClassFinderConfiguration {

		@Bean
		@SuppressWarnings("static-method")
		public EventClassFinder eventClassFinder() {

			return new AxonEventClassFinder();
		}

		public static class AxonEventClassFinder implements EventClassFinder {

			@Override
			@SuppressWarnings("synthetic-access")
			public Set<String> findEventClasses() {

				ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(false);
				componentProvider.addIncludeFilter(new AssignableTypeFilter(Object.class));

				Set<String> eventClasses = new HashSet<>();
				for (BeanDefinition bd : componentProvider.findCandidateComponents("hu.arthus")) {

					log.trace("Found class: {}", bd);

					try {
						Method[] allDeclaredMethods =
								ReflectionUtils.getAllDeclaredMethods(ClassUtils.forName(bd.getBeanClassName(), EventClassFinderConfiguration.class.getClassLoader()));

						for (Method m : allDeclaredMethods) {
							if (m.getAnnotation(EventHandler.class) != null) {

								assert m.getParameterCount() != 1;

								String eventClass = m.getParameters()[0].getType().getName();
								log.debug("Axon event class: {}", eventClass);
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
	}

}
