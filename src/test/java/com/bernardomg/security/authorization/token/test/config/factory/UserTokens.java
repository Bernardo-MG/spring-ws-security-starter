
package com.bernardomg.security.authorization.token.test.config.factory;

import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.token.model.UserToken;

public final class UserTokens {

    public static final UserToken consumed() {
        return UserToken.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withScope(UserTokenConstants.SCOPE)
            .withToken(UserTokenConstants.TOKEN)
            .withCreationDate(UserTokenConstants.DATE)
            .withExpirationDate(UserTokenConstants.DATE_FUTURE)
            .withConsumed(true)
            .withRevoked(false)
            .build();
    }

    public static final UserToken expired() {
        return UserToken.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withScope(UserTokenConstants.SCOPE)
            .withToken(UserTokenConstants.TOKEN)
            .withCreationDate(UserTokenConstants.DATE)
            .withExpirationDate(UserTokenConstants.DATE_PAST)
            .withConsumed(false)
            .withRevoked(false)
            .build();
    }

    public static final UserToken revoked() {
        return UserToken.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withScope(UserTokenConstants.SCOPE)
            .withToken(UserTokenConstants.TOKEN)
            .withCreationDate(UserTokenConstants.DATE)
            .withExpirationDate(UserTokenConstants.DATE_FUTURE)
            .withConsumed(false)
            .withRevoked(true)
            .build();
    }

    public static final UserToken valid() {
        return UserToken.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withScope(UserTokenConstants.SCOPE)
            .withToken(UserTokenConstants.TOKEN)
            .withCreationDate(UserTokenConstants.DATE)
            .withExpirationDate(UserTokenConstants.DATE_FUTURE)
            .withConsumed(false)
            .withRevoked(false)
            .build();
    }

    private UserTokens() {
        super();
    }

}
