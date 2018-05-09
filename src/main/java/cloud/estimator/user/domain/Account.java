package cloud.estimator.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

/**
 * A account.
 */

@Entity
@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Account implements Serializable {

	@Id
	@NotNull
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id", updatable = false)
	private String id;

	@OneToOne(mappedBy = "account")
	private Organization organization;

	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private Set<UserAccount> users = new HashSet<>();

	/**
	 * @param user
	 * @param authority
	 */
	public void addUser(User user, Authority authority) {
		UserAccount userAccount = new UserAccount(user, this, authority);
		users.add(userAccount);
		user.getAccounts().add(userAccount);
	}

	/**
	 * @param user
	 */
	public void removeUser(User user) {
		for (Iterator<UserAccount> iterator = users.iterator(); iterator.hasNext();) {
			UserAccount userAccount = iterator.next();

			if (userAccount.getAccount().equals(this) && userAccount.getUser().equals(user)) {
				iterator.remove();
				userAccount.getUser().getAccounts().remove(userAccount);
				userAccount.setAccount(null);
				userAccount.setUser(null);
			}
		}
	}

	@CreatedBy
	@Column(name = "created_by", nullable = false, length = 50, updatable = false)
	@JsonIgnore
	private String createdBy;

	@CreatedDate
	@Column(name = "created_date", nullable = false)
	@JsonIgnore
	@Builder.Default
	private Instant createdDate = Instant.now();

	@LastModifiedBy
	@Column(name = "last_modified_by", length = 50)
	@JsonIgnore
	private String lastModifiedBy;

	@LastModifiedDate
	@Column(name = "last_modified_date")
	@JsonIgnore
	@Builder.Default
	private Instant lastModifiedDate = Instant.now();

}
