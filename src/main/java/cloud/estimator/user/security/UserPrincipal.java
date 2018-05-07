package cloud.estimator.user.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import cloud.estimator.user.domain.User;
import cloud.estimator.user.domain.UserAccount;
import lombok.Getter;

public class UserPrincipal implements UserDetails {

  public UserPrincipal(User user) {
    this.user = user;
  }

  @Getter
  private User user;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    for (UserAccount userAccount : user.getAccounts()) {
      authorities.add(new SimpleGrantedAuthority(
          userAccount.getAccount().getId() + ":" + userAccount.getAuthority().getName()));
    }
    return authorities;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getLogin();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
