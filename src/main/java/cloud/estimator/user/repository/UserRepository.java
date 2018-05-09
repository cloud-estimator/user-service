package cloud.estimator.user.repository;

import cloud.estimator.user.domain.User;
// import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.time.Instant;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

	String USERS_BY_LOGIN_CACHE = "usersByLogin";

	String USERS_BY_EMAIL_CACHE = "usersByEmail";

	Optional<User> findOneByActivationKey(String activationKey);

	List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

	Optional<User> findOneByResetKey(String resetKey);

	Optional<User> findOneByEmailIgnoreCase(String email);

	Optional<User> findOneByLogin(String login);

	@EntityGraph(attributePaths = "accounts")
	Optional<User> findOneWithAccountsById(Long id);

	@EntityGraph(attributePaths = "accounts")
	Optional<User> findOneWithAccountsByLogin(String login);

	@EntityGraph(attributePaths = "accounts")
	Optional<User> findOneWithAccountsByEmail(String email);

	Page<User> findAllByLoginNot(Pageable pageable, String login);
}
