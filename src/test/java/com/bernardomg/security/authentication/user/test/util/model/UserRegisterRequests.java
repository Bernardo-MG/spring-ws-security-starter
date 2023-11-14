
package com.bernardomg.security.authentication.user.test.util.model;

import com.bernardomg.security.authentication.user.model.query.UserRegisterRequest;

public final class UserRegisterRequests {

    public static final UserRegisterRequest alternative() {
        return UserRegisterRequest.builder()
            .username("user")
            .name("User")
            .email(Users.ALTERNATIVE_EMAIL)
            .build();
    }

    public static final UserRegisterRequest invalidEmail() {
        return UserRegisterRequest.builder()
            .username(Users.NAME)
            .name(Users.NAME)
            .email("abc")
            .build();
    }

    public static final UserRegisterRequest missingEmail() {
        return UserRegisterRequest.builder()
            .username(Users.NAME)
            .name(Users.NAME)
            .build();
    }

    public static final UserRegisterRequest paddedWithWhitespaces() {
        return UserRegisterRequest.builder()
            .username(" admin ")
            .name(" Admin ")
            .email(" email@somewhere.com ")
            .build();
    }

    public static final UserRegisterRequest valid() {
        return UserRegisterRequest.builder()
            .username(Users.NAME)
            .name(Users.NAME)
            .email(Users.EMAIL)
            .build();
    }

    public static final UserRegisterRequest valid(final String username, final String email) {
        return UserRegisterRequest.builder()
            .username(username)
            .name(Users.NAME)
            .email(email)
            .build();
    }

}
