package hu.bankmonitor.starter.microservice.security.ldap;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "microservice-starters.security.ldap")
@Getter
@NoArgsConstructor
@Setter
@ToString(exclude = "providerPassword")
public class LdapSecurityProperties {

	private String providerUri;

	private String providerUserDn;

	private String providerPassword;

	private String userSearchBase;

	private String userSearchFilter;

	private String groupSearchBase;

	private String[] permitAllResourceMatchers;

}
