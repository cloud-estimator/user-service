package cloud.estimator.user.web.rest;

import lombok.AllArgsConstructor;
import java.security.Principal;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cloud.estimator.user.config.Constants;
import cloud.estimator.user.domain.Account;
import cloud.estimator.user.domain.User;
import cloud.estimator.user.repository.AccountRepository;
import cloud.estimator.user.repository.UserRepository;
import cloud.estimator.user.security.AuthoritiesConstants;
import cloud.estimator.user.security.SecurityUtils;
import cloud.estimator.user.service.UserService;
import cloud.estimator.user.web.util.HeaderUtil;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the User entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and
 * Authority, and send everything to the client side: there would be no View Model and DTO, a lot
 * less code, and an outer-join which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all the
 * time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li>Not having an outer join causes n+1 requests to the database. This is not a real issue as we
 * have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer
 * join (which will get lots of data from the database, for each HTTP call).</li>
 * <li>As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserResource {

  private final Logger log = LoggerFactory.getLogger(UserResource.class);

  private final UserService userService;
  private final AccountRepository aRepo;
  private final UserRepository uRepo;

  /**
   * GET /users/:login : get the "login" user.
   *
   * @param login the login of the user to find
   * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status
   *         404 (Not Found)
   */
  @GetMapping("/users")
  public ResponseEntity<Void> register() {
    log.debug("REST request to get Register : {}", "test");


    return ResponseEntity.ok().build();

  }

  @GetMapping("/users2")
  public ResponseEntity<Void> register2() {
    log.debug("REST request to get Register : {}", "test");

    Account a = aRepo.findById("8a8081966337e912016337e93d850001").get();
    System.out.println(a.getOrganization());
    a.getUsers().forEach(ua -> {
      System.out.println(ua.getUser().getId() + " : " + ua.getAuthority());

    });
    User u = uRepo.findOneByLogin("ddharna").get();
    u.getAccounts().forEach(ua -> {
      System.out.println(ua.getAccount().getId() + " : " + ua.getAuthority());
    });

    User u2 = uRepo.findOneByLogin("vdharna").get();
    u2.getAccounts().forEach(ua -> {
      System.out.println(ua.getAccount().getId() + " : " + ua.getAuthority());
    });

    return ResponseEntity.ok().build();

  }


  @SuppressWarnings("unused")
  @GetMapping("/users/invite/{accountId}")
  @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN
      + "') and @userAuthorizationService.isAuthorized(#principal, #accountId)")
  public ResponseEntity<Void> invite(@PathVariable String accountId, Principal principal) {
    log.debug("REST request to get Register : {}", "test");
    Optional<String> jwt = SecurityUtils.getCurrentUserJWT();
    Optional<String> login = SecurityUtils.getCurrentUserLogin();
    boolean isA = SecurityUtils.isAuthenticated();
    boolean isR = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);
    return ResponseEntity.ok().build();

  }

  @SuppressWarnings("unused")
  @GetMapping("/users/invite2/{accountId}")
  @PreAuthorize("hasPermission(#accountId, 'Account', '" + AuthoritiesConstants.ADMIN + "')")
  public ResponseEntity<Void> invite2(@PathVariable String accountId) {
    log.debug("REST request to get Register : {}", "test");
    Optional<String> jwt = SecurityUtils.getCurrentUserJWT();
    Optional<String> login = SecurityUtils.getCurrentUserLogin();
    boolean isA = SecurityUtils.isAuthenticated();
    boolean isR = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);
    return ResponseEntity.ok().build();

  }

  @SuppressWarnings("unused")
  @GetMapping("/users/invite3/{accountId}")
  @PreAuthorize("isAuthorizedMember(#accountId + ':" + AuthoritiesConstants.ADMIN + "')")
  public ResponseEntity<Void> invite3(@PathVariable String accountId) {
    log.debug("REST request to get Register : {}", "test");
    Optional<String> jwt = SecurityUtils.getCurrentUserJWT();
    Optional<String> login = SecurityUtils.getCurrentUserLogin();
    boolean isA = SecurityUtils.isAuthenticated();
    boolean isR = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);
    return ResponseEntity.ok().build();

  }

  @SuppressWarnings("unused")
  @GetMapping("/users/invite4/{accountId}")
  @PreAuthorize("hasAuthority(#accountId + ':" + AuthoritiesConstants.ADMIN + "')")
  public ResponseEntity<Void> invite4(@PathVariable String accountId) {
    log.debug("REST request to get Register : {}", "test");
    Optional<String> jwt = SecurityUtils.getCurrentUserJWT();
    Optional<String> login = SecurityUtils.getCurrentUserLogin();
    boolean isA = SecurityUtils.isAuthenticated();
    boolean isR = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);
    return ResponseEntity.ok().build();

  }

  /**
   * DELETE /users/:login : delete the "login" User.
   *
   * @param login the login of the user to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
  @Secured(AuthoritiesConstants.ADMIN)
  public ResponseEntity<Void> deleteUser(@PathVariable String login) {
    log.debug("REST request to delete User: {}", login);
    userService.deleteUser(login);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createAlert("A user is deleted with identifier " + login, login))
        .build();
  }
}
