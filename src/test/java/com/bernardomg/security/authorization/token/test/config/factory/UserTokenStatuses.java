
package com.bernardomg.security.authorization.token.test.config.factory;

import com.bernardomg.security.authorization.token.domain.model.UserTokenStatus;

public final class UserTokenStatuses {

    public static final UserTokenStatus invalid() {
        return UserTokenStatus.builder()
            .withValid(false)
            .build();
    }

    public static final UserTokenStatus valid() {
        return UserTokenStatus.builder()
            .withValid(true)
            .build();
    }

    private UserTokenStatuses() {
        super();
    }

}
