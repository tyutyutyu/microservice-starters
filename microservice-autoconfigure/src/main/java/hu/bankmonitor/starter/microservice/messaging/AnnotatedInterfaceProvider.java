package hu.bankmonitor.starter.microservice.messaging;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

public class AnnotatedInterfaceProvider extends ClassPathScanningCandidateComponentProvider {

	public AnnotatedInterfaceProvider(boolean useDefaultFilters) {

		super(useDefaultFilters);
	}

	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {

		return beanDefinition.getMetadata().isInterface();
	}

}
