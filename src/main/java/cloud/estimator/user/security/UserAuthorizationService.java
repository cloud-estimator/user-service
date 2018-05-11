package cloud.estimator.user.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

import java.security.Principal;


/**
 * Authenticate a user from the database.
 */
@Component("userAuthorizationService")
@AllArgsConstructor
public class UserAuthorizationService {

	private final Logger log = LoggerFactory.getLogger(UserAuthorizationService.class);

	@Transactional
	public boolean isAuthorized(final Principal principal, final String accountId) {
		log.debug("Authenticating {}", accountId);
		principal.getName();
		return true;

	}

}
