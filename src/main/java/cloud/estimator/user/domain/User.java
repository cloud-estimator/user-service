package cloud.estimator.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cloud.estimator.user.config.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.hibernate.annotations.Cache;
// import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Locale;
import java.time.Instant;

/**
 * A user.
 */

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "estimator_user")
public class User implements Serializable {

	private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

	@Id
	@NotNull
	private String id;

	@NotNull
	@Pattern(regexp = Constants.LOGIN_REGEX)
	@Size(min = 1, max = 50)
	@Column(length = 50, unique = true, nullable = false)
	private String login;

	@JsonIgnore
	@NotNull
	@Size(min = 60, max = 60)
	@Column(name = "password_hash", length = 60)
	private String password;

	@Size(max = 50)
	@Column(name = "name", length = 50)
	private String name;

	@Email
	@Size(min = 5, max = 100)
	@Column(length = 100, unique = true)
	private String email;

	@NotNull
	@Column(nullable = false)
	@Builder.Default
	private boolean activated = false;

	@Size(min = 2, max = 6)
	@Column(name = "lang_key", length = 6)
	private String langKey;

	@Size(max = 256)
	@Column(name = "image_url", length = 256)
	private String imageUrl;

	@Size(max = 20)
	@Column(name = "activation_key", length = 20)
	@JsonIgnore
	private String activationKey;

	@Size(max = 20)
	@Column(name = "reset_key", length = 20)
	@JsonIgnore
	private String resetKey;

	@Column(name = "reset_date")
	@Builder.Default
	private Instant resetDate = null;

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

	/*
	 * Below code lifted from following:
	 * https://stackoverflow.com/questions/42379899/use-custom-setter-in-lomboks-
	 * builder
	 */

	public static class UserBuilder {
		public UserBuilder password(String password) {
			this.password = _encodePassword(password);
			return this;
		}

		public UserBuilder login(String login) {
			this.login = _lowerCaseLogin(login);
			return this;
		}
	}

	// encode the password before saving it in database
	private static String _encodePassword(String password) {
		return ENCODER.encode(password);
	}

	// Lowercase the login before saving it in database
	private static String _lowerCaseLogin(String login) {
		return StringUtils.lowerCase(login, Locale.ENGLISH);
	}

}
