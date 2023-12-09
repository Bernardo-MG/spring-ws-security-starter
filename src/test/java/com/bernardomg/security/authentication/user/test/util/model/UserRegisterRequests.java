
package com.bernardomg.security.authentication.user.test.util.model;

import com.bernardomg.security.authentication.user.model.query.UserRegisterRequest;

public final class UserRegisterRequests {

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

    public static final UserRegisterRequest valid() {
        return UserRegisterRequest.builder()
            .username(Users.NAME)
            .name(Users.NAME)
            .email(Users.EMAIL)
            .build();
    }

}
