
package com.bernardomg.security.authentication.user.test.config.factory;

import java.util.List;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.authorization.role.test.config.factory.RoleEntities;

public final class UserEntities {

    public static final UserEntity disabled() {
        return UserEntity.builder()
            .withId(1L)
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withPassword(UserConstants.ENCODED_PASSWORD)
            .withEnabled(false)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

    public static final UserEntity emailChange() {
        return UserEntity.builder()
            .withId(1L)
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.ALTERNATIVE_EMAIL)
            .withPassword(UserConstants.ENCODED_PASSWORD)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

    public static final UserEntity enabled() {
        return UserEntity.builder()
            .withId(1L)
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withPassword(UserConstants.ENCODED_PASSWORD)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(false)
            .withRoles(List.of(RoleEntities.withPermission()))
            .build();
    }

    public static final UserEntity expired() {
        return UserEntity.builder()
            .withId(1L)
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withPassword(UserConstants.ENCODED_PASSWORD)
            .withEnabled(true)
            .withExpired(true)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

    public static final UserEntity locked() {
        return UserEntity.builder()
            .withId(1L)
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withPassword(UserConstants.ENCODED_PASSWORD)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(true)
            .build();
    }

    public static final UserEntity newlyCreated() {
        return UserEntity.builder()
            .withId(1L)
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withPassword("")
            .withEnabled(false)
            .withExpired(false)
            .withPasswordExpired(true)
            .withLocked(false)
            .build();
    }

    public static final UserEntity passwordExpired() {
        return UserEntity.builder()
            .withId(1L)
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withPassword(UserConstants.ENCODED_PASSWORD)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(true)
            .withLocked(false)
            .build();
    }

    private UserEntities() {
        super();
    }

}
