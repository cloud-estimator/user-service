package cloud.estimator.user.domain;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "user_account")
public class UserAccount implements Serializable {

  @EmbeddedId
  private UserAccountId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userId")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("accountId")
  private Account account;

  @ManyToOne(fetch = FetchType.LAZY)
  private Authority authority;

  public UserAccount(User user, Account account, Authority authority) {
    this.user = user;
    this.account = account;
    this.authority = authority;
    this.id = new UserAccountId(user.getId(), account.getId());
  }



}
