
package com.bernardomg.security.springframework.test.user.config.factory;

import java.util.List;

import com.bernardomg.security.domain.user.model.User;
import com.bernardomg.security.springframework.test.permission.config.factory.ResourcePermissions;

public final class Users {

    public static final User disabled() {
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME, false, true, true, true,
            List.of(Roles.withSinglePermission()), List.of(ResourcePermissions.create()));
    }

    public static final User enabled() {
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME, true, true, true, true,
            List.of(Roles.withSinglePermission()), List.of(ResourcePermissions.create()));
    }

    public static final User expired() {
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME, true, false, true, true,
            List.of(Roles.withSinglePermission()), List.of(ResourcePermissions.create()));
    }

    public static final User locked() {
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME, true, true, false, true,
            List.of(Roles.withSinglePermission()), List.of(ResourcePermissions.create()));
    }

    public static final User passwordExpired() {
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME, true, true, true, false,
            List.of(Roles.withSinglePermission()), List.of(ResourcePermissions.create()));
    }

}
