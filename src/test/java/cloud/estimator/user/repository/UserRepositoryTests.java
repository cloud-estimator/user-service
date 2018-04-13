package cloud.estimator.user.repository;

import static org.junit.Assert.*;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import cloud.estimator.user.domain.User;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@DataJpaTest
@Slf4j
public class UserRepositoryTests {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository users;
  
  @Test
  public void testFindByLastName() {
    
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    String encryptedPassword = encoder.encode("testing123");

    User user = User.builder().login("ddharna").password(encryptedPassword).firstName("Dharminder")
        .lastName("Dharna").email("dharminder@estimator.cloud").build();
    user.setCreatedBy("admin");
    entityManager.persist(user);

    Optional<User> findByLastName = users.findOneByEmailIgnoreCase(user.getEmail());
    log.info("\n\n\n-----------------");
    log.info(findByLastName.get().toString());
    log.info("\n\n\n-----------------");
    assertTrue(findByLastName.isPresent());
    
     
    

  }

}
