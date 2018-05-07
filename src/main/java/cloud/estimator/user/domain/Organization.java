package cloud.estimator.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * An organization.
 */

@Entity
@Data
@ToString(exclude = "account")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Organization implements Serializable {

	@Id
	@Column(name = "id")
	@NotNull
	private String id;

	@Size(max = 100)
	@Column(name = "name", length = 100)
	private String name;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	private Account account;

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
