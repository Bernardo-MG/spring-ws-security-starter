
package com.bernardomg.security.authentication.user.test.config.factory;

import com.bernardomg.security.authentication.user.persistence.model.UserEntity;

public final class UserEntities {

    public static final UserEntity disabled() {
        return UserEntity.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withUsername(Users.USERNAME)
            .withEmail(Users.EMAIL)
            .withPassword(Users.ENCODED_PASSWORD)
            .withEnabled(false)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

    public static final UserEntity emailChange() {
        return UserEntity.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withUsername(Users.USERNAME)
            .withEmail(Users.ALTERNATIVE_EMAIL)
            .withPassword(Users.ENCODED_PASSWORD)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

    public static final UserEntity enabled() {
        return UserEntity.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withUsername(Users.USERNAME)
            .withEmail(Users.EMAIL)
            .withPassword(Users.ENCODED_PASSWORD)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

    public static final UserEntity expired() {
        return UserEntity.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withUsername(Users.USERNAME)
            .withEmail(Users.EMAIL)
            .withPassword(Users.ENCODED_PASSWORD)
            .withEnabled(true)
            .withExpired(true)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

    public static final UserEntity locked() {
        return UserEntity.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withUsername(Users.USERNAME)
            .withEmail(Users.EMAIL)
            .withPassword(Users.ENCODED_PASSWORD)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(true)
            .build();
    }

    public static final UserEntity newlyCreated() {
        return UserEntity.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withUsername(Users.USERNAME)
            .withEmail(Users.EMAIL)
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
            .withName(Users.NAME)
            .withUsername(Users.USERNAME)
            .withEmail(Users.EMAIL)
            .withPassword(Users.ENCODED_PASSWORD)
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
