package cloud.estimator.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cloud.estimator.user.domain.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, String> {

}
