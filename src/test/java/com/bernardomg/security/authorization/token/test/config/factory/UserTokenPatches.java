
package com.bernardomg.security.authorization.token.test.config.factory;

import com.bernardomg.security.authorization.token.domain.model.UserTokenPatch;
import com.bernardomg.security.jwt.test.config.Tokens;

public final class UserTokenPatches {

    public static final UserTokenPatch changeNothing() {
        return UserTokenPatch.builder()
            .withToken(Tokens.TOKEN)
            .build();
    }

    public static final UserTokenPatch expirationDateFuture() {
        return UserTokenPatch.builder()
            .withToken(Tokens.TOKEN)
            .withExpirationDate(UserTokenConstants.DATE_MORE_FUTURE)
            .build();
    }

    public static final UserTokenPatch expirationDateYesterday() {
        return UserTokenPatch.builder()
            .withToken(Tokens.TOKEN)
            .withExpirationDate(UserTokenConstants.DATE_YESTERDAY)
            .build();
    }

    public static final UserTokenPatch notRevoked() {
        return UserTokenPatch.builder()
            .withToken(Tokens.TOKEN)
            .withRevoked(false)
            .build();
    }

    public static final UserTokenPatch revoked() {
        return UserTokenPatch.builder()
            .withToken(Tokens.TOKEN)
            .withExpirationDate(UserTokenConstants.DATE_FUTURE)
            .withRevoked(true)
            .build();
    }

    private UserTokenPatches() {
        super();
    }

}
