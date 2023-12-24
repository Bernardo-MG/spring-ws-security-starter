
package com.bernardomg.security.authentication.user.test.config.factory;

import com.bernardomg.security.authentication.user.model.query.UserRegisterRequest;

public final class UserRegisterRequests {

    public static final UserRegisterRequest invalidEmail() {
        return UserRegisterRequest.builder()
            .withUsername(Users.NAME)
            .withName(Users.NAME)
            .withEmail("abc")
            .build();
    }

    public static final UserRegisterRequest missingEmail() {
        return UserRegisterRequest.builder()
            .withUsername(Users.NAME)
            .withName(Users.NAME)
            .build();
    }

    public static final UserRegisterRequest valid() {
        return UserRegisterRequest.builder()
            .withUsername(Users.NAME)
            .withName(Users.NAME)
            .withEmail(Users.EMAIL)
            .build();
    }

}
