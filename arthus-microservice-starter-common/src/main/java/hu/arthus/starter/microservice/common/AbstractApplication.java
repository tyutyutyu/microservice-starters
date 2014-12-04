package hu.arthus.starter.microservice.common;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;

/**
 * Az alkalmazás indítását segító osztály.
 *
 * @author istvan.foldhazi
 */
@Slf4j
@SpringBootApplication
public abstract class AbstractApplication {

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

		SpringApplication app = new SpringApplication(applicationClass);

		SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
		if (!source.containsProperty("spring.profiles.active")) {
			app.setAdditionalProfiles("dev");
		}

		app.run(args);
	}

}
