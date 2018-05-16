package cloud.estimator.user.security;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import cloud.estimator.user.config.Constants;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {

		if (SecurityUtils.getCurrentUserLogin().isPresent()) {
			return SecurityUtils.getCurrentUserLogin();
		} else {
			return Optional.of(Constants.SYSTEM_ACCOUNT);
		}
	}
}
