package cloud.estimator.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import lombok.AllArgsConstructor;

@Configuration
@EnableResourceServer
@EnableAuthorizationServer
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web
		  .ignoring()
		    .antMatchers(HttpMethod.OPTIONS, "/**")
		    .antMatchers("/app/**/*.{js,html}")
		    .antMatchers("/i18n/**")
		    .antMatchers("/content/**")
		    .antMatchers("/swagger-ui/index.html")
		    .antMatchers("/test/**")
			.antMatchers("/h2-console/**")
			.antMatchers("/api/register**");
	}
	

}