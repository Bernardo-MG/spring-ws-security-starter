
package com.bernardomg.security.user.test.config.factory;

import java.util.List;

import com.bernardomg.security.role.test.config.factory.RoleEntities;
import com.bernardomg.security.user.data.adapter.inbound.jpa.model.UserEntity;

public final class UserEntities {

    public static final UserEntity additionalRole() {
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
            .withRoles(List.of(RoleEntities.withPermission(), RoleEntities.alternative()))
            .withLoginAttempts(0)
            .build();
    }

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
            .withLoginAttempts(0)
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
            .withLoginAttempts(0)
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
            .withLoginAttempts(0)
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
            .withLoginAttempts(0)
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
            .withLoginAttempts(0)
            .build();
    }

    public static final UserEntity nameChange() {
        return UserEntity.builder()
            .withId(1L)
            .withName(UserConstants.ALTERNATIVE_NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            .withPassword(UserConstants.ENCODED_PASSWORD)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(false)
            .withRoles(List.of(RoleEntities.withPermission()))
            .withLoginAttempts(0)
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
            .withLoginAttempts(0)
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
            .withLoginAttempts(0)
            .build();
    }

    public static final UserEntity updatedPassword() {
        return UserEntity.builder()
            .withId(1L)
            .withName(UserConstants.NAME)
            .withUsername(UserConstants.USERNAME)
            .withEmail(UserConstants.EMAIL)
            // FIXME: this is not the newly encoded password
            .withPassword(UserConstants.ENCODED_NEW_PASSWORD)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(false)
            .withRoles(List.of(RoleEntities.withPermission()))
            .withLoginAttempts(0)
            .build();
    }

    public static final UserEntity withoutRoles() {
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
            .withRoles(List.of())
            .withLoginAttempts(0)
            .build();
    }

    private UserEntities() {
        super();
    }

}
