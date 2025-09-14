
package com.bernardomg.security.user.test.config.factory;

import java.util.List;

import com.bernardomg.security.role.test.config.factory.RoleEntities;
import com.bernardomg.security.user.adapter.inbound.jpa.model.UserEntity;

public final class UserEntities {

    public static final UserEntity additionalRole() {
        final UserEntity entity;

        entity = new UserEntity();
        entity.setId(1L);
        entity.setName(UserConstants.NAME);
        entity.setUsername(UserConstants.USERNAME);
        entity.setEmail(UserConstants.EMAIL);
        entity.setPassword(UserConstants.ENCODED_PASSWORD);
        entity.setEnabled(true);
        entity.setNotExpired(true);
        entity.setPasswordNotExpired(true);
        entity.setNotLocked(true);
        entity.setRoles(List.of(RoleEntities.withPermission(), RoleEntities.alternative()));
        entity.setLoginAttempts(0);

        return entity;
    }

    public static final UserEntity disabled() {
        final UserEntity entity;

        entity = new UserEntity();
        entity.setId(1L);
        entity.setName(UserConstants.NAME);
        entity.setUsername(UserConstants.USERNAME);
        entity.setEmail(UserConstants.EMAIL);
        entity.setPassword(UserConstants.ENCODED_PASSWORD);
        entity.setEnabled(false);
        entity.setNotExpired(true);
        entity.setPasswordNotExpired(true);
        entity.setNotLocked(true);
        entity.setLoginAttempts(0);

        return entity;
    }

    public static final UserEntity emailChange() {
        final UserEntity entity;

        entity = new UserEntity();
        entity.setId(1L);
        entity.setName(UserConstants.NAME);
        entity.setUsername(UserConstants.USERNAME);
        entity.setEmail(UserConstants.ALTERNATIVE_EMAIL);
        entity.setPassword(UserConstants.ENCODED_PASSWORD);
        entity.setEnabled(true);
        entity.setNotExpired(true);
        entity.setPasswordNotExpired(true);
        entity.setNotLocked(true);
        entity.setLoginAttempts(0);

        return entity;
    }

    public static final UserEntity enabled() {
        final UserEntity entity;

        entity = new UserEntity();
        entity.setId(1L);
        entity.setName(UserConstants.NAME);
        entity.setUsername(UserConstants.USERNAME);
        entity.setEmail(UserConstants.EMAIL);
        entity.setPassword(UserConstants.ENCODED_PASSWORD);
        entity.setEnabled(true);
        entity.setNotExpired(true);
        entity.setPasswordNotExpired(true);
        entity.setNotLocked(true);
        entity.setRoles(List.of(RoleEntities.withPermission()));
        entity.setLoginAttempts(0);

        return entity;
    }

    public static final UserEntity expired() {
        final UserEntity entity;

        entity = new UserEntity();
        entity.setId(1L);
        entity.setName(UserConstants.NAME);
        entity.setUsername(UserConstants.USERNAME);
        entity.setEmail(UserConstants.EMAIL);
        entity.setPassword(UserConstants.ENCODED_PASSWORD);
        entity.setEnabled(true);
        entity.setNotExpired(false);
        entity.setPasswordNotExpired(true);
        entity.setNotLocked(true);
        entity.setLoginAttempts(0);

        return entity;
    }

    public static final UserEntity locked() {
        final UserEntity entity;

        entity = new UserEntity();
        entity.setId(1L);
        entity.setName(UserConstants.NAME);
        entity.setUsername(UserConstants.USERNAME);
        entity.setEmail(UserConstants.EMAIL);
        entity.setPassword(UserConstants.ENCODED_PASSWORD);
        entity.setEnabled(true);
        entity.setNotExpired(true);
        entity.setPasswordNotExpired(true);
        entity.setNotLocked(false);
        entity.setRoles(List.of(RoleEntities.withPermission()));
        entity.setLoginAttempts(0);

        return entity;
    }

    public static final UserEntity nameChange() {
        final UserEntity entity;

        entity = new UserEntity();
        entity.setId(1L);
        entity.setName(UserConstants.ALTERNATIVE_NAME);
        entity.setUsername(UserConstants.USERNAME);
        entity.setEmail(UserConstants.EMAIL);
        entity.setPassword(UserConstants.ENCODED_PASSWORD);
        entity.setEnabled(true);
        entity.setNotExpired(true);
        entity.setPasswordNotExpired(true);
        entity.setNotLocked(true);
        entity.setRoles(List.of(RoleEntities.withPermission()));
        entity.setLoginAttempts(0);

        return entity;
    }

    public static final UserEntity newlyCreated() {
        final UserEntity entity;

        entity = new UserEntity();
        entity.setId(1L);
        entity.setName(UserConstants.NAME);
        entity.setUsername(UserConstants.USERNAME);
        entity.setEmail(UserConstants.EMAIL);
        entity.setPassword("");
        entity.setEnabled(false);
        entity.setNotExpired(true);
        entity.setPasswordNotExpired(false);
        entity.setNotLocked(true);
        entity.setLoginAttempts(0);

        return entity;
    }

    public static final UserEntity passwordExpired() {
        final UserEntity entity;

        entity = new UserEntity();
        entity.setId(1L);
        entity.setName(UserConstants.NAME);
        entity.setUsername(UserConstants.USERNAME);
        entity.setEmail(UserConstants.EMAIL);
        entity.setPassword(UserConstants.ENCODED_PASSWORD);
        entity.setEnabled(true);
        entity.setNotExpired(true);
        entity.setPasswordNotExpired(false);
        entity.setNotLocked(true);
        entity.setLoginAttempts(0);

        return entity;
    }

    public static final UserEntity updatedPassword() {
        final UserEntity entity;

        entity = new UserEntity();
        entity.setId(1L);
        entity.setName(UserConstants.NAME);
        entity.setUsername(UserConstants.USERNAME);
        entity.setEmail(UserConstants.EMAIL);
        // FIXME: this is not the newly encoded password
        entity.setPassword(UserConstants.ENCODED_NEW_PASSWORD);
        entity.setEnabled(true);
        entity.setNotExpired(true);
        entity.setPasswordNotExpired(true);
        entity.setNotLocked(true);
        entity.setRoles(List.of(RoleEntities.withPermission()));
        entity.setLoginAttempts(0);

        return entity;
    }

    public static final UserEntity withoutRoles() {
        final UserEntity entity;

        entity = new UserEntity();
        entity.setId(1L);
        entity.setName(UserConstants.NAME);
        entity.setUsername(UserConstants.USERNAME);
        entity.setEmail(UserConstants.EMAIL);
        entity.setPassword(UserConstants.ENCODED_PASSWORD);
        entity.setEnabled(true);
        entity.setNotExpired(true);
        entity.setPasswordNotExpired(true);
        entity.setNotLocked(true);
        entity.setRoles(List.of());
        entity.setLoginAttempts(0);

        return entity;
    }

    private UserEntities() {
        super();
    }

}
