
package com.bernardomg.security.authentication.user.test.util.model;

import com.bernardomg.security.authentication.user.model.User;

public final class Users {

    public static String ALTERNATIVE_EMAIL = "mail2@somewhere.com";

    public static String EMAIL             = "mail@somewhere.com";

    public static String ENCODED_PASSWORD  = "$2a$04$gV.k/KKIqr3oPySzs..bx.8absYRTpNe8AbHmPP90.ErW0ICGOsVW";

    public static String NAME              = "Admin";

    public static String PASSWORD          = "1234";

    public static String USERNAME          = "admin";

    public static final User disabled() {
        return User.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withUsername(Users.USERNAME)
            .withEmail(Users.EMAIL)
            .withEnabled(false)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

    public static final User emailChange() {
        return User.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withUsername(Users.USERNAME)
            .withEmail(Users.ALTERNATIVE_EMAIL)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

    public static final User enabled() {
        return User.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withUsername(Users.USERNAME)
            .withEmail(Users.EMAIL)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

    public static final User expired() {
        return User.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withUsername(Users.USERNAME)
            .withEmail(Users.EMAIL)
            .withEnabled(true)
            .withExpired(true)
            .withPasswordExpired(false)
            .withLocked(false)
            .build();
    }

    public static final User locked() {
        return User.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withUsername(Users.USERNAME)
            .withEmail(Users.EMAIL)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(false)
            .withLocked(true)
            .build();
    }

    public static final User newlyCreated() {
        return User.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withUsername(Users.USERNAME)
            .withEmail(Users.EMAIL)
            .withEnabled(false)
            .withExpired(false)
            .withPasswordExpired(true)
            .withLocked(false)
            .build();
    }

    public static final User passwordExpired() {
        return User.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withUsername(Users.USERNAME)
            .withEmail(Users.EMAIL)
            .withEnabled(true)
            .withExpired(false)
            .withPasswordExpired(true)
            .withLocked(false)
            .build();
    }

}
