package hu.bankmonitor.starter.microservice.security.ldap;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.InetOrgPersonContextMapper;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;

@ConditionalOnClass({ LdapAuthenticationProvider.class })
@ConditionalOnMissingBean(LdapWebSecurityAutoConfiguration.class)
@Configuration
@EnableConfigurationProperties(LdapSecurityProperties.class)
@EnableWebSecurity
@Slf4j
public class LdapWebSecurityAutoConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private LdapSecurityProperties properties;

	@PostConstruct
	public void init() {

		log.debug("init - properties: {}", properties);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// @formatter:off
		http
			.authorizeRequests()
				.antMatchers(properties.getPermitAllResourceMatchers()).permitAll()
				.anyRequest().fullyAuthenticated()
				.and()
			.formLogin();
		// @formatter:on
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		// @formatter:off
		auth
			.authenticationProvider(authenticationProvider());
		// @formatter:on
	}

	@Bean
	@Override
	protected UserDetailsService userDetailsService() {

		return new LdapUserDetailsService(ldapUserSearch(), authoritiesPopulator());
	}

	@Bean
	DefaultLdapAuthoritiesPopulator authoritiesPopulator() {

		DefaultLdapAuthoritiesPopulator authoritiesPopulator = new DefaultLdapAuthoritiesPopulator(contextSource(), properties.getGroupSearchBase());
		authoritiesPopulator.setSearchSubtree(true);

		return authoritiesPopulator;
	}

	@Bean
	BindAuthenticator authenticator() {

		BindAuthenticator authenticator = new BindAuthenticator(contextSource());
		authenticator.setUserSearch(ldapUserSearch());

		return authenticator;
	}

	@Bean
	AuthenticationProvider authenticationProvider() {

		LdapAuthenticationProvider authenticationProvider = new LdapAuthenticationProvider(authenticator(), authoritiesPopulator());
		authenticationProvider.setUserDetailsContextMapper(new InetOrgPersonContextMapper());

		return authenticationProvider;
	}

	@Bean
	LdapUserSearch ldapUserSearch() {

		return new FilterBasedLdapUserSearch(properties.getUserSearchBase(), properties.getUserSearchFilter(), contextSource());
	}

	@Bean
	DefaultSpringSecurityContextSource contextSource() {

		DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(properties.getProviderUri());
		contextSource.setUserDn(properties.getProviderUserDn());
		contextSource.setPassword(properties.getProviderPassword());

		return contextSource;
	}

}
