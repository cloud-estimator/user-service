package cloud.estimator.user.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode(of = {"userId", "accountId"})
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountId implements Serializable {

  @Column(name = "user_id")
  private String userId;

  @Column(name = "account_id")
  private String accountId;


}
