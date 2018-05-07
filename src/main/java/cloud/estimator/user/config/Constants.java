package cloud.estimator.user.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Application constants.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "en";
    
}
