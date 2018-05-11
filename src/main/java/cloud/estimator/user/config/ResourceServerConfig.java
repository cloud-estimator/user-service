package cloud.estimator.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Configuration
@EnableResourceServer
@Import(SecurityProblemSupport.class)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private SecurityProblemSupport problemSupport;


	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				.exceptionHandling()
				.authenticationEntryPoint(problemSupport)
				.accessDeniedHandler(problemSupport)
				.and()
				.requestMatchers()
				.and()
				.authorizeRequests()
				.antMatchers("/api/allow", "/api/register", "/api-docs/**").permitAll()
				.anyRequest().authenticated();
	}
}
