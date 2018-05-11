package cloud.estimator.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import cloud.estimator.user.domain.User;
import cloud.estimator.user.repository.UserRepository;
import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor
@Component
@Primary
public class JWTTokenEnhancer implements TokenEnhancer {

	@Autowired
	private UserRepository repository;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

		// fetch the list of accounts this user is allowed to access
		Optional<User> user = repository.findOneWithAccountsByLogin(authentication.getName());
		Map<String, Object> additionalInfo = new HashMap<>();
		String orgId = "1";

		additionalInfo.put("organizationId", orgId);

		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		return accessToken;
	}
}
