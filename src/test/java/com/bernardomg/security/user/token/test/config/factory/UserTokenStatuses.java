
package com.bernardomg.security.user.token.test.config.factory;

import com.bernardomg.security.user.domain.model.UserTokenStatus;

public final class UserTokenStatuses {

    public static final UserTokenStatus invalid() {
        return new UserTokenStatus("", false);
    }

    public static final UserTokenStatus valid() {
        return new UserTokenStatus("", true);
    }

    private UserTokenStatuses() {
        super();
    }

}
