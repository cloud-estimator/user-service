package cloud.estimator.user.web.rest.vm;

import javax.validation.constraints.*;
import cloud.estimator.user.config.Constants;
import cloud.estimator.user.security.AuthoritiesConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.Instant;

/**
 * A DTO representing a user, with his authorities.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVM {

	public static final int PASSWORD_MIN_LENGTH = 4;
	public static final int PASSWORD_MAX_LENGTH = 100;

	private String id;

	@NotBlank
	@Pattern(regexp = Constants.LOGIN_REGEX)
	@Size(min = 1, max = 50)
	private String login;

	@Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
	@Getter
	private String password;

	@Size(max = 100)
	private String name;

	@Email
	@Size(min = 5, max = 100)
	private String email;

	@Size(max = 256)
	private String imageUrl;

	private boolean activated;

	@Size(min = 2, max = 6)
	private String langKey;

	private String createdBy;

	private Instant createdDate;

	private String lastModifiedBy;

	private Instant lastModifiedDate;
	
	private String accountId;

	private AuthoritiesConstants authority;

}
