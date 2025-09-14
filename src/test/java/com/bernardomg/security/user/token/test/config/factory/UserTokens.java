
package com.bernardomg.security.user.token.test.config.factory;

import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.user.data.domain.model.UserToken;
import com.bernardomg.security.user.test.config.factory.UserConstants;

public final class UserTokens {

    public static final UserToken alternative() {
        return new UserToken(UserConstants.USERNAME, UserConstants.NAME, Tokens.SCOPE, Tokens.ALTERNATIVE_TOKEN,
            UserTokenConstants.DATE, UserTokenConstants.DATE_FUTURE, false, false);
    }

    public static final UserToken changeNothing() {
        return new UserToken(null, null, null, Tokens.TOKEN, null, null, null, null);
    }

    public static final UserToken consumed() {
        return new UserToken(UserConstants.USERNAME, UserConstants.NAME, Tokens.SCOPE, Tokens.TOKEN,
            UserTokenConstants.DATE, UserTokenConstants.DATE_FUTURE, true, false);
    }

    public static final UserToken expirationDateFuture() {
        return new UserToken(null, null, null, Tokens.TOKEN, null, UserTokenConstants.DATE_MORE_FUTURE, null, null);
    }

    public static final UserToken expirationDateYesterday() {
        return new UserToken(null, null, null, Tokens.TOKEN, null, UserTokenConstants.DATE_YESTERDAY, null, null);
    }

    public static final UserToken expired() {
        return new UserToken(UserConstants.USERNAME, UserConstants.NAME, Tokens.SCOPE, Tokens.TOKEN,
            UserTokenConstants.DATE, UserTokenConstants.DATE_PAST, false, false);
    }

    public static final UserToken future() {
        return new UserToken(UserConstants.USERNAME, UserConstants.NAME, Tokens.SCOPE, Tokens.TOKEN,
            UserTokenConstants.DATE, UserTokenConstants.DATE_MORE_FUTURE, false, false);
    }

    public static final UserToken notRevoked() {
        return new UserToken(null, null, null, Tokens.TOKEN, null, null, false, false);
    }

    public static final UserToken outOfScope() {
        return new UserToken(UserConstants.USERNAME, UserConstants.NAME, Tokens.ALTERNATIVE_SCOPE, Tokens.TOKEN,
            UserTokenConstants.DATE, UserTokenConstants.DATE_FUTURE, false, false);
    }

    public static final UserToken revoked() {
        return new UserToken(UserConstants.USERNAME, UserConstants.NAME, Tokens.SCOPE, Tokens.TOKEN,
            UserTokenConstants.DATE, UserTokenConstants.DATE_FUTURE, false, true);
    }

    public static final UserToken valid() {
        return new UserToken(UserConstants.USERNAME, UserConstants.NAME, Tokens.SCOPE, Tokens.TOKEN,
            UserTokenConstants.DATE, UserTokenConstants.DATE_FUTURE, false, false);
    }

    private UserTokens() {
        super();
    }

}
