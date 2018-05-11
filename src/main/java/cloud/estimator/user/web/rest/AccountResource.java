package cloud.estimator.user.web.rest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import cloud.estimator.user.repository.UserRepository;
import cloud.estimator.user.service.UserService;
import cloud.estimator.user.web.rest.errors.EmailAlreadyUsedException;
import cloud.estimator.user.web.rest.errors.InvalidPasswordException;
import cloud.estimator.user.web.rest.errors.LoginAlreadyUsedException;
import cloud.estimator.user.web.rest.vm.UserVM;
import lombok.RequiredArgsConstructor;
import javax.validation.Valid;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountResource {

	private final Logger log = LoggerFactory.getLogger(AccountResource.class);

	private final UserRepository userRepository;

	private final UserService userService;

	/**
	 * POST /register : register the user.
	 *
	 * @param managedUserVM
	 *            the managed user View Model
	 * @throws InvalidPasswordException
	 *             400 (Bad Request) if the password is incorrect
	 * @throws EmailAlreadyUsedException
	 *             400 (Bad Request) if the email is already used
	 * @throws LoginAlreadyUsedException
	 *             400 (Bad Request) if the login is already used
	 */
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public void registerAccount(@Valid @RequestBody UserVM managedUserVM) {
		if (!checkPasswordLength(managedUserVM.getPassword())) {
			throw new InvalidPasswordException();
		}
		userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).ifPresent(u -> {
			throw new LoginAlreadyUsedException();
		});
		userRepository.findOneByEmailIgnoreCase(managedUserVM.getEmail()).ifPresent(u -> {
			throw new EmailAlreadyUsedException();
		});
		userService.registerUser(managedUserVM);
		// mailService.sendActivationEmail(user);
	}

	@GetMapping("/allow")
	public void allow() {
		System.out.println("Reached");
	}

	@GetMapping("/deny")
	public void deny() {
		System.out.println("Reached");
	}

	private static boolean checkPasswordLength(String password) {
		return !StringUtils.isEmpty(password) && password.length() >= UserVM.PASSWORD_MIN_LENGTH
				&& password.length() <= UserVM.PASSWORD_MAX_LENGTH;
	}
}
