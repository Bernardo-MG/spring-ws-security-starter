
package com.bernardomg.security.authentication.user.test.util.model;

import com.bernardomg.security.authentication.user.model.query.UserRegister;
import com.bernardomg.security.authentication.user.model.query.UserRegisterRequest;

public final class UsersCreate {

    public static final UserRegister alternative() {
        return UserRegisterRequest.builder()
            .username("user")
            .name("User")
            .email("email2@somewhere.com")
            .build();
    }

    public static final UserRegister invalidEmail() {
        return UserRegisterRequest.builder()
            .username("admin")
            .name("Admin")
            .email("abc")
            .build();
    }

    public static final UserRegister missingEmail() {
        return UserRegisterRequest.builder()
            .username("admin")
            .name("Admin")
            .build();
    }

    public static final UserRegister paddedWithWhitespaces() {
        return UserRegisterRequest.builder()
            .username(" admin ")
            .name(" Admin ")
            .email(" email@somewhere.com ")
            .build();
    }

    public static final UserRegister valid() {
        return UserRegisterRequest.builder()
            .username("admin")
            .name("Admin")
            .email("email@somewhere.com")
            .build();
    }

    public static final UserRegister valid(final String username, final String email) {
        return UserRegisterRequest.builder()
            .username(username)
            .name("Admin")
            .email(email)
            .build();
    }

}
