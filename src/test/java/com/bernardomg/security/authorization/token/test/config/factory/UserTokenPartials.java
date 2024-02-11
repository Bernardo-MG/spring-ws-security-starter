
package com.bernardomg.security.authorization.token.test.config.factory;

import com.bernardomg.security.authorization.token.domain.model.request.UserTokenPartial;

public final class UserTokenPartials {

    public static final UserTokenPartial empty() {
        return UserTokenPartial.builder()
            .build();
    }

    public static final UserTokenPartial expirationDate() {
        return UserTokenPartial.builder()
            .withExpirationDate(UserTokenConstants.DATE_MORE_FUTURE)
            .build();
    }

    public static final UserTokenPartial expirationDateYesterday() {
        return UserTokenPartial.builder()
            .withExpirationDate(UserTokenConstants.DATE_YESTERDAY)
            .build();
    }

    public static final UserTokenPartial notRevoked() {
        return UserTokenPartial.builder()
            .withRevoked(false)
            .build();
    }

    public static final UserTokenPartial revoked() {
        return UserTokenPartial.builder()
            .withRevoked(true)
            .build();
    }

    private UserTokenPartials() {
        super();
    }

}
