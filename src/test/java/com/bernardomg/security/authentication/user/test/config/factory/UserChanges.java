
package com.bernardomg.security.authentication.user.test.config.factory;

import com.bernardomg.security.authentication.user.model.UserChange;

public final class UserChanges {

    public static final UserChange disabled() {
        return UserChange.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withEmail(UserConstants.EMAIL)
            .withEnabled(false)
            .withPasswordExpired(false)
            .build();
    }

    public static final UserChange emailChange() {
        return UserChange.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withEmail(UserConstants.ALTERNATIVE_EMAIL)
            .withEnabled(true)
            .withPasswordExpired(false)
            .build();
    }

    public static final UserChange emailChangeUpperCase() {
        return UserChange.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withEmail(UserConstants.ALTERNATIVE_EMAIL.toUpperCase())
            .withEnabled(true)
            .withPasswordExpired(false)
            .build();
    }

    public static final UserChange enabled() {
        return UserChange.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withEmail(UserConstants.EMAIL)
            .withEnabled(true)
            .withPasswordExpired(false)
            .build();
    }

    public static final UserChange invalidEmail() {
        return UserChange.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withEmail("abc")
            .withEnabled(true)
            .withPasswordExpired(false)
            .build();
    }

    public static final UserChange nameChange() {
        return UserChange.builder()
            .withUsername(UserConstants.USERNAME)
            .withName("Admin2")
            .withEmail(UserConstants.EMAIL)
            .withEnabled(true)
            .withPasswordExpired(false)
            .build();
    }

    public static final UserChange noEmail() {
        return UserChange.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withEnabled(true)
            .withPasswordExpired(false)
            .build();
    }

    public static final UserChange noEnabled() {
        return UserChange.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withEmail(UserConstants.EMAIL)
            .withPasswordExpired(false)
            .build();
    }

    public static final UserChange noUsername() {
        return UserChange.builder()
            .withName(UserConstants.NAME)
            .withEmail(UserConstants.EMAIL)
            .withEnabled(true)
            .withPasswordExpired(false)
            .build();
    }

    public static final UserChange paddedWithWhitespaces() {
        return UserChange.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(" " + UserConstants.NAME + " ")
            .withEmail(" " + UserConstants.ALTERNATIVE_EMAIL + " ")
            .withEnabled(true)
            .withPasswordExpired(false)
            .build();
    }

    public static final UserChange passwordExpired() {
        return UserChange.builder()
            .withUsername(UserConstants.USERNAME)
            .withName(UserConstants.NAME)
            .withEmail(UserConstants.EMAIL)
            .withEnabled(true)
            .withPasswordExpired(true)
            .build();
    }

}
