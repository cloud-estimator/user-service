package cloud.estimator.user.repository;

import static org.junit.Assert.*;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import cloud.estimator.user.domain.User;
import cloud.estimator.user.security.AuthoritiesConstants;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@DataJpaTest
@Slf4j
public class UserRepositoryTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	public void testFindByLastName() {

		Optional<User> findByEmail = userRepository.findOneByLogin("ddharna");
		assertTrue(findByEmail.isPresent());
		User foundUser = findByEmail.get();

		log.info("\n\n\n-----------------");
		log.info(foundUser.toString());
		log.info("\n\n\n-----------------");

		assertTrue(StringUtils.isAllLowerCase(foundUser.getLogin()));

	}

	@Test
	public void testHasAccounts() {

		Optional<User> findByEmail = userRepository.findOneByLogin("ddharna");
		assertTrue(findByEmail.isPresent());
		User foundUser = findByEmail.get();

		log.info("\n\n\n-----------------");
		log.info(foundUser.toString());
		log.info("\n\n\n-----------------");

		assertTrue(foundUser.getAccounts().size() > 0);
		assertTrue(
				foundUser.getAccounts().iterator().next().getAuthority().getName().equals(AuthoritiesConstants.ADMIN));

	}

}
