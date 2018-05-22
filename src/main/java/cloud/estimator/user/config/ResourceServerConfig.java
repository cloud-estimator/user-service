package cloud.estimator.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import cloud.estimator.user.security.AuthoritiesConstants;


@Configuration
@EnableResourceServer
@Import(SecurityProblemSupport.class)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private SecurityProblemSupport problemSupport;


	@Override
	public void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.exceptionHandling()
			.authenticationEntryPoint(problemSupport)
			.accessDeniedHandler(problemSupport)
		.and()
            .csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable()
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
			.authorizeRequests()
			.antMatchers("/api/allow").permitAll()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/account/reset-password/init").permitAll()
            .antMatchers("/api/account/reset-password/finish").permitAll()
            .antMatchers("/api/profile-info").permitAll()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN);
		// @formatter:on
	}
}
