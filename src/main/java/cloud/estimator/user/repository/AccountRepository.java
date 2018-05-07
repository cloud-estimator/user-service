package cloud.estimator.user.repository;

import cloud.estimator.user.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Account entity.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {


}
