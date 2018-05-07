package cloud.estimator.user.security;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import cloud.estimator.user.domain.User;
import cloud.estimator.user.repository.UserRepository;
import java.util.Locale;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for DomainUserDetailsService.
 *
 * @see DomainUserDetailsService
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserDetailsServiceIntTest {

  private static final String USER_ONE_LOGIN = "test-user-one";
  private static final String USER_ONE_EMAIL = "test-user-one@localhost";
  private static final String USER_TWO_LOGIN = "test-user-two";
  private static final String USER_TWO_EMAIL = "test-user-two@localhost";
  private static final String USER_THREE_LOGIN = "test-user-three";
  private static final String USER_THREE_EMAIL = "test-user-three@localhost";

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserDetailsService userDetailsService;

  private User userOne;
  private User userTwo;
  private User userThree;

  @Before
  public void init() {
    userOne =
        User.builder().login(USER_ONE_LOGIN).password(RandomStringUtils.random(8)).activated(true)
            .email(USER_ONE_EMAIL).name("userOne doe").langKey("en").createdBy("junit").build();

    userRepository.save(userOne);

    userTwo =
        User.builder().login(USER_TWO_LOGIN).password(RandomStringUtils.random(8)).activated(true)
            .email(USER_TWO_EMAIL).name("userTwo doe").langKey("en").createdBy("junit").build();

    userRepository.save(userTwo);

    userThree =
        User.builder()
          .login(USER_THREE_LOGIN)
          .password(RandomStringUtils.random(8))
          .activated(false)
          .email(USER_THREE_EMAIL)
          .name("userThree doe")
          .langKey("en")
          .createdBy("junit")
          .build();

    userRepository.save(userThree);

  }

  @Test
  @Transactional
  public void assertThatUserCanBeFoundByLogin() {
    UserDetails userDetails = userDetailsService.loadUserByUsername(USER_ONE_LOGIN);
    assertThat(userDetails).isNotNull();
    assertThat(userDetails.getUsername()).isEqualTo(USER_ONE_LOGIN);
  }

  @Test
  @Transactional
  public void assertThatUserCanBeFoundByLoginIgnoreCase() {
    UserDetails userDetails =
        userDetailsService.loadUserByUsername(USER_ONE_LOGIN.toUpperCase(Locale.ENGLISH));
    assertThat(userDetails).isNotNull();
    assertThat(userDetails.getUsername()).isEqualTo(USER_ONE_LOGIN);
  }

  @Test
  @Transactional
  public void assertThatUserCanBeFoundByEmail() {
    UserDetails userDetails = userDetailsService.loadUserByUsername(USER_TWO_EMAIL);
    assertThat(userDetails).isNotNull();
    assertThat(userDetails.getUsername()).isEqualTo(USER_TWO_LOGIN);
  }

  @Test
  @Transactional
  public void assertThatUserCanBeFoundByEmailIgnoreCase() {
    UserDetails userDetails =
        userDetailsService.loadUserByUsername(USER_TWO_EMAIL.toUpperCase(Locale.ENGLISH));
    assertThat(userDetails).isNotNull();
    assertThat(userDetails.getUsername()).isEqualTo(USER_TWO_LOGIN);
  }

  @Test
  @Transactional
  public void assertThatEmailIsPrioritizedOverLogin() {
    UserDetails userDetails =
        userDetailsService.loadUserByUsername(USER_ONE_EMAIL.toUpperCase(Locale.ENGLISH));
    assertThat(userDetails).isNotNull();
    assertThat(userDetails.getUsername()).isEqualTo(USER_ONE_LOGIN);
  }

  @Test(expected = UserNotActivatedException.class)
  @Transactional
  public void assertThatUserNotActivatedExceptionIsThrownForNotActivatedUsers() {
    userDetailsService.loadUserByUsername(USER_THREE_LOGIN);
  }

}
