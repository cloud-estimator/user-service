package cloud.estimator.user.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import cloud.estimator.user.domain.Account;
import cloud.estimator.user.domain.Organization;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@DataJpaTest
@Slf4j
public class AccountRepositoryTests {

	@Autowired
	private AccountRepository accounts;

	@Autowired
	private OrganizationRepository organizations;
	
	@Test
	public void createAccount() {

		Account account = Account.builder()
				.createdBy("anon")
				.build();

		accounts.save(account);
		assertNotNull(account.getId());

		Organization organization = Organization.builder()
				.name("GG Consulting")
				.build();

		accounts.save(account);
		assertNotNull(account.getId());
		
		organization.setAccount(account);
		account.setOrganization(organization);
		
		organizations.save(organization);
		accounts.save(account);
		
		assertEquals(account.getId(), organization.getId());

		log.info("\n\n\n-----------------");
		log.info(account.toString());
		log.info(account.getOrganization().toString());
		log.info("\n\n\n-----------------");

	}

	@Test
	public void testFindById() {

		Optional<Account> findById = accounts.findById("8a8081966337e912016337e93d850001");
		assertTrue(findById.isPresent());
		Account foundAccount = findById.get();

		log.info("\n\n\n-----------------");
		log.info(foundAccount.toString());
		log.info("\n\n\n-----------------");

	}

}
