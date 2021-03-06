package web.securityconfig;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CompositeFilter;

import web.member.service.CustomUserDetailsService;

@EnableWebSecurity
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	OAuth2ClientContext oauth2ClientContext;
	
	@Override
	public void configure(WebSecurity web)
	{
		web.ignoring().antMatchers("/css/**", "/script/**", "image/**", "/fonts/**", "lib/**");
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.authorizeRequests()
			.antMatchers("/admin/**").hasRole("ADMIN")
			.antMatchers("/board/**").authenticated()
			.antMatchers("/**").permitAll()
			.and().formLogin()
			.loginPage("/login")
			.loginProcessingUrl("/login")
			.defaultSuccessUrl("/")
			.successHandler(successHandler())
    			.failureUrl("/login")
    			.and()
    			.logout()
    			.and()
    			.addFilterBefore((javax.servlet.Filter) ssoFilter(), BasicAuthenticationFilter.class);
	}
	
	@Bean
	public AuthenticationSuccessHandler successHandler() {
	    return new CustomLoginSuccessHandler("/defaultUrl");
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
	}
	
	
	@SuppressWarnings("unchecked")
	private Filter ssoFilter() {
	  CompositeFilter filter = new CompositeFilter();
	  List<Filter> filters = new ArrayList<>();
	  filters.add(ssoFilter(facebook(), "/login/facebook"));
	  filters.add(ssoFilter(github(), "/login/github"));
	  filter.setFilters((List<? extends javax.servlet.Filter>) filters);
	  return (Filter) filter;
	}
	
	private Filter ssoFilter(ClientResources client, String path) {
	  OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(path);
	  OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
	  filter.setRestTemplate(template);
	  UserInfoTokenServices tokenServices = new UserInfoTokenServices(
	      client.getResource().getUserInfoUri(), client.getClient().getClientId());
	  tokenServices.setRestTemplate(template);
	  filter.setTokenServices(tokenServices);
	  return (Filter) filter;
	}
	
	class ClientResources {
	  @NestedConfigurationProperty
	  private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();
	  @NestedConfigurationProperty
	  private ResourceServerProperties resource = new ResourceServerProperties();
	  public AuthorizationCodeResourceDetails getClient() {
	    return client;
	  }
	  public ResourceServerProperties getResource() {
	    return resource;
	  }
	}
	
	@Bean
	@ConfigurationProperties("github")
	public ClientResources github() {
	  return new ClientResources();
	}
	@Bean
	@ConfigurationProperties("facebook")
	public ClientResources facebook() {
	  return new ClientResources();
	}
	
	@Bean
	public FilterRegistrationBean oauth2ClientFilterRegistration(
	    OAuth2ClientContextFilter filter) {
	  FilterRegistrationBean registration = new FilterRegistrationBean();
	  registration.setFilter(filter);
	  registration.setOrder(-100);
	  return registration;
	}
}