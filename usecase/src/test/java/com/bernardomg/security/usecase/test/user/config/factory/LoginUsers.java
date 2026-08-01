
package com.bernardomg.security.usecase.test.user.config.factory;

import java.util.List;

import com.bernardomg.security.usecase.login.domain.LoginUser;

public final class LoginUsers {

    public static final LoginUser valid() {
        return new LoginUser(UserConstants.ID, UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME,
            List.of(ResourcePermissions.create()));
    }

    private LoginUsers() {
        super();
    }

}
