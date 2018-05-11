package cloud.estimator.user.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cloud.estimator.user.domain.Account;
import cloud.estimator.user.domain.Authority;
import cloud.estimator.user.domain.User;
import cloud.estimator.user.repository.AccountRepository;
import cloud.estimator.user.repository.AuthorityRepository;
import cloud.estimator.user.repository.UserRepository;
import cloud.estimator.user.security.AuthoritiesConstants;
import cloud.estimator.user.security.SecurityUtils;
import cloud.estimator.user.service.util.RandomUtil;
import cloud.estimator.user.web.rest.vm.UserVM;
import lombok.AllArgsConstructor;

/**
 * Service class for managing users.
 */
@Service
@Transactional
@AllArgsConstructor
public class UserService {

	private final Logger log = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final AccountRepository accountRepository;
	private final AuthorityRepository authorityRepository;

	@Transactional(readOnly = true)
	public Optional<User> getUserByLogin(String login) {
		return userRepository.findOneByLogin(login);
	}

	public User registerUser(UserVM userVM) {

		User newUser = User.builder()
				.activated(false)
				.createdBy(AuthoritiesConstants.ANONYMOUS)
				.login(userVM.getLogin())
				.password(userVM.getPassword())
				.name(userVM.getName())
				.email(userVM.getEmail())
				.imageUrl(userVM.getImageUrl())
				.langKey(userVM.getLangKey())
				// new user is not active
				.activated(false)
				// new user gets registration key
				.activationKey(RandomUtil.generateActivationKey()).build();

		userRepository.save(newUser);

		Account account = accountRepository.getOne(userVM.getAccountId());
		Authority authority = authorityRepository.getOne(AuthoritiesConstants.USER);

		SecurityUtils.getCurrentUserJWT();
		SecurityUtils.getCurrentUserLogin();
		SecurityUtils.isAuthenticated();

		account.addUser(newUser, authority);

		log.debug("Created Information for User: {}", newUser);
		return newUser;
	}

	public void deleteUser(String login) {
		userRepository.findOneByLogin(login).ifPresent(user -> {
			userRepository.delete(user);
			log.debug("Deleted User: {}", user);
		});
	}

}
