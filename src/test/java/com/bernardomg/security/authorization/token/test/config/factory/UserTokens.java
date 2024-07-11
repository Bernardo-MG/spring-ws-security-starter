
package com.bernardomg.security.authorization.token.test.config.factory;

import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.token.domain.model.UserToken;
import com.bernardomg.security.jwt.test.config.Tokens;

public final class UserTokens {

    public static final UserToken alternative() {
        return UserToken.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withScope(Tokens.SCOPE)
            .withToken(Tokens.ALTERNATIVE_TOKEN)
            .withCreationDate(UserTokenConstants.DATE)
            .withExpirationDate(UserTokenConstants.DATE_FUTURE)
            .withConsumed(false)
            .withRevoked(false)
            .build();
    }

    public static final UserToken consumed() {
        return UserToken.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withScope(Tokens.SCOPE)
            .withToken(Tokens.TOKEN)
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
            .withScope(Tokens.SCOPE)
            .withToken(Tokens.TOKEN)
            .withCreationDate(UserTokenConstants.DATE)
            .withExpirationDate(UserTokenConstants.DATE_PAST)
            .withConsumed(false)
            .withRevoked(false)
            .build();
    }

    public static final UserToken future() {
        return UserToken.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withScope(Tokens.SCOPE)
            .withToken(Tokens.TOKEN)
            .withCreationDate(UserTokenConstants.DATE)
            .withExpirationDate(UserTokenConstants.DATE_MORE_FUTURE)
            .withConsumed(false)
            .withRevoked(false)
            .build();
    }

    public static final UserToken outOfScope() {
        return UserToken.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withScope(Tokens.ALTERNATIVE_SCOPE)
            .withToken(Tokens.TOKEN)
            .withCreationDate(UserTokenConstants.DATE)
            .withExpirationDate(UserTokenConstants.DATE_FUTURE)
            .withConsumed(false)
            .withRevoked(false)
            .build();
    }

    public static final UserToken revoked() {
        return UserToken.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withScope(Tokens.SCOPE)
            .withToken(Tokens.TOKEN)
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
            .withScope(Tokens.SCOPE)
            .withToken(Tokens.TOKEN)
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
