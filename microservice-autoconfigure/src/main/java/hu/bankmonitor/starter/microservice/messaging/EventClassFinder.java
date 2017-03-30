package hu.bankmonitor.starter.microservice.messaging;

import com.google.common.collect.ImmutableMap;
import hu.bankmonitor.starter.microservice.common.errorhandling.ExceptionData;
import hu.bankmonitor.starter.microservice.common.errorhandling.ExceptionType;
import hu.bankmonitor.starter.microservice.common.errorhandling.MicroserviceStarterRuntimeException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

@Slf4j
public class EventClassFinder {

	private static final String MESSAGE_HANDLER_METHOD_NAME = "handleMessage";

	private final String basePackage;

	public EventClassFinder(String basePackage) {

		this.basePackage = basePackage;
	}

	public Set<String> findEventClasses() {

		Set<String> eventClassesByHandlers = findEventClassesByHandlers();
		Set<String> eventClassesByInheritance = findEventClassesByInheritance();

		if (!eventClassesByInheritance.containsAll(eventClassesByHandlers)) {
			// @formatter:off
			throw new MicroserviceStarterRuntimeException(
				ExceptionData.builder()
					.type(ExceptionType.EVENT_CLASS_FINDER_FIND_EVENT_CLASSES_ERROR)
					.data(ImmutableMap.of(
							"eventClassesByHandlers", eventClassesByHandlers,
							"eventClassesByInheritance", eventClassesByInheritance))
					.build());
			// @formatter:on
		}

		return eventClassesByHandlers;
	}

	private Set<String> findEventClassesByHandlers() {

		ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(false);
		componentProvider.addIncludeFilter(new AssignableTypeFilter(AbstractMessageHandler.class));

		Set<String> eventClasses = new HashSet<>();
		for (BeanDefinition bd : componentProvider.findCandidateComponents(basePackage)) {

			try {
				Method[] allDeclaredMethods = ReflectionUtils.getAllDeclaredMethods(ClassUtils.forName(bd.getBeanClassName(), this.getClass().getClassLoader()));

				for (Method method : allDeclaredMethods) {
					if (MESSAGE_HANDLER_METHOD_NAME.equals(method.getName())) {

						if (method.getParameterCount() != 1) {
							// @formatter:off
							throw new MicroserviceStarterRuntimeException(
								ExceptionData.builder()
									.type(ExceptionType.EVENT_CLASS_FINDER_WRONG_METHOD_SIGNITURE)
									.data(ImmutableMap.of(
											"className", bd.getBeanClassName(),
											"method", method.toGenericString()))
									.build());
							// @formatter:on
						}

						String eventClass = method.getParameters()[0].getType().getName();
						log.debug("Handled event class: {}", eventClass);
						eventClasses.add(eventClass);
					}
				}

			} catch (ClassNotFoundException | LinkageError e) {
				// @formatter:off
				throw new MicroserviceStarterRuntimeException(
					ExceptionData.builder()
						.type(ExceptionType.EVENT_CLASS_FINDER_CLASS_LOADING_ERROR)
						.cause(e)
						.data(ImmutableMap.of("className", bd.getBeanClassName()))
						.build());
				// @formatter:on
			}
		}

		log.trace("findEventClassesByHandlers - result: {}", eventClasses);
		return eventClasses;
	}

	private Set<String> findEventClassesByInheritance() {

		ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(false);
		componentProvider.addIncludeFilter(new AssignableTypeFilter(AbstractEvent.class));

		Set<String> eventClasses = componentProvider.findCandidateComponents(basePackage).stream().map(bd -> bd.getBeanClassName()).collect(Collectors.toSet());

		log.trace("findEventClassesByInheritance - result: {}", eventClasses);
		return eventClasses;
	}

}
