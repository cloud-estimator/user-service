package cloud.estimator.user.repository;

import static org.junit.Assert.assertTrue;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import cloud.estimator.user.domain.Account;
import cloud.estimator.user.domain.Authority;
import cloud.estimator.user.security.AuthoritiesConstants;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@DataJpaTest
@Slf4j
public class AuthorityRepositoryTests {

	@Autowired
	private AuthorityRepository authorities;

	@Test
	public void testFindByName() {

		Authority foundAuthority = authorities.getOne(AuthoritiesConstants.USER);
		log.info("\n\n\n-----------------");
		log.info(foundAuthority.toString());
		log.info("\n\n\n-----------------");

	}

}
