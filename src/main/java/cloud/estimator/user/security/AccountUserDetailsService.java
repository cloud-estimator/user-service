package cloud.estimator.user.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cloud.estimator.user.domain.User;
import cloud.estimator.user.repository.UserRepository;
import lombok.AllArgsConstructor;

import java.util.Locale;
import java.util.Optional;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
@AllArgsConstructor
public class AccountUserDetailsService implements UserDetailsService {

	private final Logger log = LoggerFactory.getLogger(AccountUserDetailsService.class);

	private final UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String login) {
		log.debug("Authenticating {}", login);
		String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
		Optional<User> userByEmailFromDatabase = userRepository.findOneWithAccountsByEmail(lowercaseLogin);
		return userByEmailFromDatabase.map(user -> createSpringSecurityUser(lowercaseLogin, user)).orElseGet(() -> {
			Optional<User> userByLoginFromDatabase = userRepository.findOneWithAccountsByLogin(lowercaseLogin);
			return userByLoginFromDatabase.map(user -> createSpringSecurityUser(lowercaseLogin, user))
					.orElseThrow(() -> new UsernameNotFoundException(
							"User " + lowercaseLogin + " was not found in the " + "database"));
		});

	}

	/**
	 * @param lowercaseLogin
	 * @param user
	 * @return
	 */
	private UserPrincipal createSpringSecurityUser(String lowercaseLogin, User user) {
		if (!user.isActivated()) {
			throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
		}
		return new UserPrincipal(user);
	}

}
