package cloud.estimator.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cloud.estimator.user.domain.Authority;

/**
 * Spring Data JPA repository for the Account entity.
 */
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {

}
