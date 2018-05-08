package cloud.estimator.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cloud.estimator.user.domain.UserAccount;
import cloud.estimator.user.domain.UserAccountId;

public interface UserAccountRepository extends JpaRepository<UserAccount, UserAccountId> {

}
