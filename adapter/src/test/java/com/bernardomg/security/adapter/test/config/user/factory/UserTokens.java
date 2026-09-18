
package com.bernardomg.security.adapter.test.config.user.factory;

import com.bernardomg.security.adapter.test.config.jwt.factory.Tokens;
import com.bernardomg.security.domain.user.model.UserToken;

public final class UserTokens {

    public static final UserToken alternative() {
        return new UserToken(UserConstants.USERNAME, UserConstants.NAME, Tokens.ALTERNATIVE_SCOPE,
            Tokens.ALTERNATIVE_TOKEN, Tokens.CREATION_DATE, Tokens.EXPIRATION, false, false);
    }

    public static final UserToken consumed() {
        return new UserToken(UserConstants.USERNAME, UserConstants.NAME, Tokens.SCOPE, Tokens.TOKEN,
            Tokens.CREATION_DATE, Tokens.EXPIRATION, true, false);
    }

    public static final UserToken expired() {
        return new UserToken(UserConstants.USERNAME, UserConstants.NAME, Tokens.SCOPE, Tokens.TOKEN,
            Tokens.CREATION_DATE, Tokens.EXPIRED, false, false);
    }

    public static final UserToken revoked() {
        return new UserToken(UserConstants.USERNAME, UserConstants.NAME, Tokens.SCOPE, Tokens.TOKEN,
            Tokens.CREATION_DATE, Tokens.EXPIRATION, false, true);
    }

    public static final UserToken valid() {
        return new UserToken(UserConstants.USERNAME, UserConstants.NAME, Tokens.SCOPE, Tokens.TOKEN,
            Tokens.CREATION_DATE, Tokens.EXPIRATION, false, false);
    }

    private UserTokens() {
        super();
    }

}
