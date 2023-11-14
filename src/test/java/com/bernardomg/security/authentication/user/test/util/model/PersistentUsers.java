
package com.bernardomg.security.authentication.user.test.util.model;

import com.bernardomg.security.authentication.user.persistence.model.PersistentUser;

public final class PersistentUsers {

    public static final PersistentUser disabled() {
        return PersistentUser.builder()
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

    public static final PersistentUser enabled() {
        return PersistentUser.builder()
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

    public static final PersistentUser expired() {
        return PersistentUser.builder()
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

    public static final PersistentUser locked() {
        return PersistentUser.builder()
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

    public static final PersistentUser passwordExpired() {
        return PersistentUser.builder()
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
