package hu.bankmonitor.starter.microservice.messaging;

import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.remoting.client.AmqpProxyFactoryBean;
import org.springframework.amqp.remoting.service.AmqpInvokerServiceExporter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

@Slf4j
@Configuration
public class RpcServiceConfiguration implements BeanDefinitionRegistryPostProcessor {

	private static final String BASE_PACKAGE = "hu";

	private ConfigurableListableBeanFactory beanFactory;

	private BeanDefinitionRegistry registry;

	@Autowired(required = false)
	private List<? extends RpcService> rpcServices;

	// private final static String AMQP_PROXY_FACTORY_SUFFIX = "_AMQP_PROXY_FACTORY";

	private final static String AMQP_PROXY_SUFFIX = "_AMQP_PROXY";

	private final static String AMQP_SERVICE_EXPORTER_SUFFIX = "_SERVICE_EXPORTER";

	private void registerRpcClients() {

		AnnotatedInterfaceProvider interfaceProvider = new AnnotatedInterfaceProvider(false);
		interfaceProvider.addIncludeFilter(new AnnotationTypeFilter(RpcServiceClient.class));
		for (BeanDefinition bd : interfaceProvider.findCandidateComponents(BASE_PACKAGE)) {

			// String proxyFactoryName = bd.getBeanClassName() + AMQP_PROXY_FACTORY_SUFFIX;
			String proxyName = bd.getBeanClassName() + AMQP_PROXY_SUFFIX;

			try {
				Class serviceClass = Class.forName(bd.getBeanClassName());
				BeanDefinitionBuilder amqpProxyFactoryBuilder = BeanDefinitionBuilder.rootBeanDefinition(AmqpProxyFactoryBean.class);
				amqpProxyFactoryBuilder.addPropertyValue("serviceInterface", serviceClass);
				amqpProxyFactoryBuilder.addDependsOn("rabbitTemplate");
				amqpProxyFactoryBuilder.addPropertyReference("amqpTemplate", "rabbitTemplate");
				registry.registerBeanDefinition(proxyName, amqpProxyFactoryBuilder.getBeanDefinition());

				// BeanDefinitionBuilder serviceProxyBuilder = BeanDefinitionBuilder.rootBeanDefinition(serviceClass);
				// serviceProxyBuilder.addDependsOn(proxyFactoryName);
				// serviceProxyBuilder.getRawBeanDefinition().setFactoryBeanName(proxyFactoryName);
				// serviceProxyBuilder.getRawBeanDefinition().setFactoryMethodName("getObject");
				// // serviceProxyBuilder.setFactoryMethod("getObject");
				// registry.registerBeanDefinition(proxyName, serviceProxyBuilder.getBeanDefinition());

			} catch (ClassNotFoundException e) {
				log.debug("amqp proxy service cannot be created :{}, exception: {}", bd.getBeanClassName(), e.getMessage());
			}

		}
	}

	private void registerRpcServers() {

		if (rpcServices != null) {
			for (RpcService rpcService : rpcServices) {
				Class rpcServiceInterface = Arrays.stream(ClassUtils.getAllInterfaces(rpcService)).filter(c -> c.getAnnotation(RpcServiceServer.class) != null).findFirst().get();
				BeanDefinitionBuilder amqpServiceExporterBuilder = BeanDefinitionBuilder.genericBeanDefinition(AmqpInvokerServiceExporter.class);

				amqpServiceExporterBuilder.addPropertyValue("serviceInterface", rpcServiceInterface);
				amqpServiceExporterBuilder.addPropertyValue("service", rpcService);
				amqpServiceExporterBuilder.addPropertyReference("amqpTemplate", "rabbitTemplate");
				registry.registerBeanDefinition(rpcServiceInterface.getName() + AMQP_SERVICE_EXPORTER_SUFFIX, amqpServiceExporterBuilder.getBeanDefinition());

			}
		}
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		this.beanFactory = beanFactory;
		registerRpcServers();

	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

		this.registry = registry;
		registerRpcClients();

		log.debug("Rpc services registered.");
	}

}
