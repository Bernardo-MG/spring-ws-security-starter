
package com.bernardomg.security.authorization.token.test.config.factory;

import com.bernardomg.security.authorization.token.adapter.inbound.jpa.model.UserTokenEntity;
import com.bernardomg.security.jwt.test.config.Tokens;

public final class UserTokenEntities {

    public static final UserTokenEntity alternative() {
        return UserTokenEntity.builder()
            .withId(1L)
            .withUserId(1L)
            .withScope(Tokens.SCOPE)
            .withToken(Tokens.ALTERNATIVE_TOKEN)
            .withCreationDate(UserTokenConstants.DATE)
            .withExpirationDate(UserTokenConstants.DATE_FUTURE)
            .withConsumed(false)
            .withRevoked(false)
            .build();
    }

    public static final UserTokenEntity revoked() {
        return UserTokenEntity.builder()
            .withId(1L)
            .withUserId(1L)
            .withScope(Tokens.SCOPE)
            .withToken(Tokens.TOKEN)
            .withCreationDate(UserTokenConstants.DATE)
            .withExpirationDate(UserTokenConstants.DATE_FUTURE)
            .withConsumed(false)
            .withRevoked(true)
            .build();
    }

    public static final UserTokenEntity valid() {
        return UserTokenEntity.builder()
            .withId(1L)
            .withUserId(1L)
            .withScope(Tokens.SCOPE)
            .withToken(Tokens.TOKEN)
            .withCreationDate(UserTokenConstants.DATE)
            .withExpirationDate(UserTokenConstants.DATE_FUTURE)
            .withConsumed(false)
            .withRevoked(false)
            .build();
    }

    private UserTokenEntities() {
        super();
    }

}
