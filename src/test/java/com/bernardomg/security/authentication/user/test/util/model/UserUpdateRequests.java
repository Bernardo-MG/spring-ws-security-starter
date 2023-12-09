
package com.bernardomg.security.authentication.user.test.util.model;

import com.bernardomg.security.authentication.user.model.query.UserUpdateRequest;

public final class UserUpdateRequests {

    public static final UserUpdateRequest disabled() {
        return UserUpdateRequest.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withEmail(Users.EMAIL)
            .withEnabled(false)
            .withPasswordExpired(false)
            .build();
    }

    public static final UserUpdateRequest emailChange() {
        return UserUpdateRequest.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withEmail(Users.ALTERNATIVE_EMAIL)
            .withEnabled(true)
            .withPasswordExpired(false)
            .build();
    }

    public static final UserUpdateRequest emailChangeUpperCase() {
        return UserUpdateRequest.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withEmail(Users.ALTERNATIVE_EMAIL.toUpperCase())
            .withEnabled(true)
            .withPasswordExpired(false)
            .build();
    }

    public static final UserUpdateRequest enabled() {
        return UserUpdateRequest.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withEmail(Users.EMAIL)
            .withEnabled(true)
            .withPasswordExpired(false)
            .build();
    }

    public static final UserUpdateRequest invalidEmail() {
        return UserUpdateRequest.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withEmail("abc")
            .withEnabled(true)
            .withPasswordExpired(false)
            .build();
    }

    public static final UserUpdateRequest nameChange() {
        return UserUpdateRequest.builder()
            .withId(1L)
            .withName("Admin2")
            .withEmail(Users.EMAIL)
            .withEnabled(true)
            .withPasswordExpired(false)
            .build();
    }

    public static final UserUpdateRequest noEmail() {
        return UserUpdateRequest.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withEnabled(true)
            .withPasswordExpired(false)
            .build();
    }

    public static final UserUpdateRequest noEnabled() {
        return UserUpdateRequest.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withEmail(Users.EMAIL)
            .withPasswordExpired(false)
            .build();
    }

    public static final UserUpdateRequest noId() {
        return UserUpdateRequest.builder()
            .withName(Users.NAME)
            .withEmail(Users.EMAIL)
            .withEnabled(true)
            .withPasswordExpired(false)
            .build();
    }

    public static final UserUpdateRequest paddedWithWhitespaces() {
        return UserUpdateRequest.builder()
            .withId(1L)
            .withName(" " + Users.NAME + " ")
            .withEmail(" " + Users.ALTERNATIVE_EMAIL + " ")
            .withEnabled(true)
            .withPasswordExpired(false)
            .build();
    }

    public static final UserUpdateRequest passwordExpired() {
        return UserUpdateRequest.builder()
            .withId(1L)
            .withName(Users.NAME)
            .withEmail(Users.EMAIL)
            .withEnabled(true)
            .withPasswordExpired(true)
            .build();
    }

}
