
package com.bernardomg.security.usecase.test.user.config.factory;

import com.bernardomg.security.domain.user.model.UserToken;

public final class UserTokens {

    public static final UserToken revoked() {
        return new UserToken(UserTokenConstants.USERNAME, UserTokenConstants.NAME, UserTokenConstants.SCOPE,
            UserTokenConstants.TOKEN, UserTokenConstants.CREATION_DATE, UserTokenConstants.EXPIRATION_DATE, false,
            true);
    }

    public static final UserToken valid() {
        return new UserToken(UserTokenConstants.USERNAME, UserTokenConstants.NAME, UserTokenConstants.SCOPE,
            UserTokenConstants.TOKEN, UserTokenConstants.CREATION_DATE, UserTokenConstants.EXPIRATION_DATE, false,
            false);
    }

    private UserTokens() {
        super();
    }

}
