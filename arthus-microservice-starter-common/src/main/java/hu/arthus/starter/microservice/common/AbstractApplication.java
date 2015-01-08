package hu.arthus.starter.microservice.common;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;

/**
 * Az alkalmazás indítását segítő osztály.
 *
 * @author istvan.foldhazi
 */
@Slf4j
@SpringBootApplication
public abstract class AbstractApplication {

	public static enum RequestWebEnvironment {
		TRUE, FALSE, AUTO;
	}

	@Autowired
	private Environment env;

	/**
	 * Spring profiles can be configured with a program arguments --spring.profiles.active=your-active-profile
	 *
	 */
	@PostConstruct
	public void postConstruct() {

		if (env.getActiveProfiles().length == 0) {
			log.warn("No Spring profile configured, running with default configuration");
		} else {
			log.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));
		}
	}

	/**
	 * E függvényen keresztül lehet indítani az alkalmazást.
	 *
	 * @param applicationClass
	 *            Az main metódust tartalmazó osztály típusa. (Általában a hívó osztály.)
	 * @param args
	 *            A hívó osztály main függvényének paramétere.
	 */
	public static void start(Class<?> applicationClass, String[] args) {

		start(applicationClass, RequestWebEnvironment.AUTO, args);
	}

	/**
	 * E függvényen keresztül lehet indítani az alkalmazást.
	 *
	 * @param applicationClass
	 *            Az main metódust tartalmazó osztály típusa. (Általában a hívó osztály.)
	 * @param requestWebEnvironment
	 *            Flag to explicitly request a web or non-web environment or auto.
	 * @param args
	 *            A hívó osztály main függvényének paramétere.
	 */
	public static void start(Class<?> applicationClass, RequestWebEnvironment requestWebEnvironment, String[] args) {

		SpringApplicationBuilder builder = new SpringApplicationBuilder(applicationClass);
		if (requestWebEnvironment == RequestWebEnvironment.TRUE) {
			builder = builder.web(true);
		} else if (requestWebEnvironment == RequestWebEnvironment.FALSE) {
			builder = builder.web(false);
		}
		SpringApplication app = builder.build();

		SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
		log.debug("cli profiles: {}", source.getProperty("spring.profiles.active"));

		if (!source.containsProperty("spring.profiles.active")) {
			app.setAdditionalProfiles("dev");
		}

		app.run(args);
	}
}
