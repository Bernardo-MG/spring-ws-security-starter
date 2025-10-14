
package com.bernardomg.security.user.test.config.factory;

import java.util.List;

import org.springframework.context.i18n.LocaleContextHolder;

import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.test.config.factory.RoleConstants;
import com.bernardomg.security.role.test.config.factory.Roles;
import com.bernardomg.security.user.domain.model.User;

public final class Users {

    public static final User additionalRole() {
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME, true, true, true, true,
            List.of(Roles.withSinglePermission(), Roles.alternative()));
    }

    public static final User addRole() {
        final Role role1;
        final Role role2;

        role1 = new Role(RoleConstants.NAME, List.of());
        role2 = new Role(RoleConstants.ALTERNATIVE_NAME, List.of());
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME, true, true, true, true,
            List.of(role1, role2));
    }

    public static final User disabled() {
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME, false, true, true, true,
            List.of(Roles.withSinglePermission()));
    }

    public static final User duplicatedRole() {
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME, true, true, true, true,
            List.of(Roles.withSinglePermission(), Roles.withSinglePermission()));
    }

    public static final User emailChange() {
        return new User(UserConstants.ALTERNATIVE_EMAIL, UserConstants.USERNAME, UserConstants.NAME, true, true, true,
            true, List.of(Roles.withSinglePermission()));
    }

    public static final User enabled() {
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME, true, true, true, true,
            List.of(Roles.withSinglePermission()));
    }

    public static final User expired() {
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME, true, false, true, true,
            List.of(Roles.withSinglePermission()));
    }

    public static final User invalidEmail() {
        return new User("abc", UserConstants.USERNAME, UserConstants.NAME, true, true, true, true, List.of());
    }

    public static final User locked() {
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME, true, true, false, true,
            List.of(Roles.withSinglePermission()));
    }

    public static final User newlyCreated() {
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME, false, true, true, false,
            List.of());
    }

    public static final User newlyCreatedWithRole() {
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME, false, true, true, false,
            List.of(Roles.withSinglePermission()));
    }

    public static final User noName() {
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, "", true, true, true, true,
            List.of(Roles.withSinglePermission()));
    }

    public static final User padded() {
        return new User(" " + UserConstants.EMAIL + " ", " " + UserConstants.USERNAME + " ",
            " " + UserConstants.NAME + " ", true, true, true, false, List.of());
    }

    public static final User passwordExpired() {
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME, true, true, true, false,
            List.of(Roles.withSinglePermission()));
    }

    public static final User passwordExpiredAndDisabled() {
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME, false, true, true, false,
            List.of(Roles.withSinglePermission()));
    }

    public static final User upperCase() {
        return new User(UserConstants.EMAIL.toUpperCase(LocaleContextHolder.getLocale()),
            UserConstants.USERNAME.toUpperCase(LocaleContextHolder.getLocale()),
            UserConstants.NAME.toUpperCase(LocaleContextHolder.getLocale()), true, true, true, false, List.of());
    }

    public static final User withoutPermissions() {
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME, true, true, true, true,
            List.of(Roles.withoutPermissions()));
    }

    public static final User withoutRoles() {
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME, true, true, true, true,
            List.of());
    }

    public static final User withRole() {
        return new User(UserConstants.EMAIL, UserConstants.USERNAME, UserConstants.NAME, true, true, true, true,
            List.of(Roles.withSinglePermission()));
    }

}
