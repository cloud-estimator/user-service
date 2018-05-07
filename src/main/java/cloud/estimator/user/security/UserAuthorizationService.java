package cloud.estimator.user.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cloud.estimator.user.domain.User;
import cloud.estimator.user.repository.UserRepository;
import lombok.AllArgsConstructor;

import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

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
