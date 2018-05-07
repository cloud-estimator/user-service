package cloud.estimator.user.repository;

import static org.junit.Assert.assertTrue;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import cloud.estimator.user.domain.Account;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@DataJpaTest
@Slf4j
public class AccountRepositoryTests {

  @Autowired
  private AccountRepository accounts;

  @Test
  public void testFindByLastName() {

    Optional<Account> findById = accounts.findById("1");
    assertTrue(findById.isPresent());
    Account foundAccount = findById.get();

    log.info("\n\n\n-----------------");
    log.info(foundAccount.toString());
    log.info("\n\n\n-----------------");

  }

}
