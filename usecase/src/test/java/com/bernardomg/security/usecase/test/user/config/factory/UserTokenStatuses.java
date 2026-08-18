
package com.bernardomg.security.usecase.test.user.config.factory;

import com.bernardomg.security.domain.user.model.UserTokenStatus;

public final class UserTokenStatuses {

    public static final UserTokenStatus invalid() {
        return new UserTokenStatus(UserTokenConstants.USERNAME, false);
    }

    public static final UserTokenStatus invalidMissingUsername() {
        return new UserTokenStatus("", false);
    }

    public static final UserTokenStatus valid() {
        return new UserTokenStatus(UserTokenConstants.USERNAME, true);
    }

    public static final UserTokenStatus validMissingUsername() {
        return new UserTokenStatus("", true);
    }

    private UserTokenStatuses() {
        super();
    }

}
