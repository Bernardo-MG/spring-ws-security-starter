
package com.bernardomg.security.authentication.user.test.util.model;

import com.bernardomg.security.authentication.user.persistence.model.UserEntity;

public final class PersistentUsers {

    public static final UserEntity disabled() {
        return UserEntity.builder()
            .id(1L)
            .name(Users.NAME)
            .username(Users.USERNAME)
            .email(Users.EMAIL)
            .password(Users.PASSWORD)
            .enabled(false)
            .expired(false)
            .passwordExpired(false)
            .locked(false)
            .build();
    }

    public static final UserEntity enabled() {
        return UserEntity.builder()
            .id(1L)
            .name(Users.NAME)
            .username(Users.USERNAME)
            .email(Users.EMAIL)
            .password(Users.PASSWORD)
            .enabled(true)
            .expired(false)
            .passwordExpired(false)
            .locked(false)
            .build();
    }

    public static final UserEntity expired() {
        return UserEntity.builder()
            .id(1L)
            .name(Users.NAME)
            .username(Users.USERNAME)
            .email(Users.EMAIL)
            .password(Users.PASSWORD)
            .enabled(true)
            .expired(true)
            .passwordExpired(false)
            .locked(false)
            .build();
    }

    public static final UserEntity locked() {
        return UserEntity.builder()
            .id(1L)
            .name(Users.NAME)
            .username(Users.USERNAME)
            .email(Users.EMAIL)
            .password(Users.PASSWORD)
            .enabled(true)
            .expired(false)
            .passwordExpired(false)
            .locked(true)
            .build();
    }

    public static final UserEntity passwordExpired() {
        return UserEntity.builder()
            .id(1L)
            .name(Users.NAME)
            .username(Users.USERNAME)
            .email(Users.EMAIL)
            .password(Users.PASSWORD)
            .enabled(true)
            .expired(false)
            .passwordExpired(true)
            .locked(false)
            .build();
    }

    private PersistentUsers() {
        super();
    }

}
