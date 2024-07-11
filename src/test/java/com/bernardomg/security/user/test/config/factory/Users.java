
package com.bernardomg.security.user.test.config.factory;

import java.util.List;

import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.security.user.data.domain.model.User;

public final class Users {

    public static final User additionalRole() {
        return User.builder()
            .withRoles(List.of(Roles.withSinglePermission(), Roles.alternative()))
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

    public static final User addRole() {
        return User.builder()
            .withRoles(List.of(Role.builder()
                .withName(RoleConstants.NAME)
                .build(),
                Role.builder()
                    .withName(RoleConstants.ALTERNATIVE_NAME)
                    .build()))
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

    public static final User disabled() {
        return User.builder()
            .withRoles(List.of(Roles.withSinglePermission()))
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withEnabled(false)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

    public static final User duplicatedRole() {
        return User.builder()
            .withRoles(List.of(Roles.withSinglePermission(), Roles.withSinglePermission()))
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

    public static final User emailChange() {
        return User.builder()
            .withRoles(List.of(Roles.withSinglePermission()))
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.ALTERNATIVE_EMAIL)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

    public static final User enabled() {
        return User.builder()
            .withRoles(List.of(Roles.withSinglePermission()))
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

    public static final User expired() {
        return User.builder()
            .withRoles(List.of(Roles.withSinglePermission()))
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withEnabled(true)
            .withExpired(true)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

    public static final User locked() {
        return User.builder()
            .withRoles(List.of(Roles.withSinglePermission()))
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(true)
            .build();
    }

    public static final User newlyCreated() {
        return User.builder()
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withEnabled(false)
            .withExpired(false)
            .withPasswordExpired(true)
            .withLocked(false)
            .build();
    }

    public static final User newlyCreatedWithRole() {
        return User.builder()
            .withRoles(List.of(Roles.withSinglePermission()))
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withEnabled(false)
            .withExpired(false)
            .withPasswordExpired(true)
            .withLocked(false)
            .build();
    }

    public static final User noRoles() {
        return User.builder()
            .withRoles(List.of())
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

    public static final User passwordExpired() {
        return User.builder()
            .withRoles(List.of(Roles.withSinglePermission()))
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(true)
            .withLocked(false)
            .build();
    }

    public static final User passwordExpiredAndDisabled() {
        return User.builder()
            .withRoles(List.of(Roles.withSinglePermission()))
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withEnabled(false)
            .withExpired(false)
            .withPasswordExpired(true)
            .withLocked(false)
            .build();
    }

    public static final User withoutPermissions() {
        return User.builder()
            .withRoles(List.of(Roles.withoutPermissions()))
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

    public static final User withoutRoles() {
        return User.builder()
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

}
