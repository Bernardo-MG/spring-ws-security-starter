
package com.bernardomg.security.springframework.test.login.usecase.config.factory;

import java.util.List;

import com.bernardomg.security.springframework.test.permission.config.factory.ResourcePermissions;
import com.bernardomg.security.springframework.test.user.config.factory.UserConstants;
import com.bernardomg.security.usecase.login.domain.LoginUser;

public final class LoginUsers {

    public static final LoginUser noPermissions() {
        return new LoginUser(UserConstants.ID, UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME,
            List.of());
    }

    public static final LoginUser valid() {
        return new LoginUser(UserConstants.ID, UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME,
            List.of(ResourcePermissions.create()));
    }

    private LoginUsers() {
        super();
    }

}
