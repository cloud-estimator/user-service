package cloud.estimator.user.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cloud.estimator.user.domain.User;
import cloud.estimator.user.repository.UserRepository;
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
	
    @Transactional(readOnly = true)
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findOneByLogin(login);
    }


	public void deleteUser(String login) {
		userRepository.findOneByLogin(login).ifPresent(user -> {
			userRepository.delete(user);
			log.debug("Deleted User: {}", user);
		});
	}
	
	
}
