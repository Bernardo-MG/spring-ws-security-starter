
package com.bernardomg.security.adapter.outbound.mail.test.user.config.factory;

import java.util.List;

import com.bernardomg.security.domain.user.model.User;

public final class Users {

    public static final User enabled() {
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME, true, true, true, true,
            List.of(Roles.withSinglePermission()));
    }

    private Users() {
        super();
    }

}
