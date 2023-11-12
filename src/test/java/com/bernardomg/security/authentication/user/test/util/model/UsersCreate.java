
package com.bernardomg.security.authentication.user.test.util.model;

import com.bernardomg.security.authentication.user.model.query.UserCreate;
import com.bernardomg.security.authentication.user.model.query.UserCreateRequest;

public final class UsersCreate {

    public static final UserCreate alternative() {
        return UserCreateRequest.builder()
            .username("user")
            .name("User")
            .email("email2@somewhere.com")
            .build();
    }

    public static final UserCreate invalidEmail() {
        return UserCreateRequest.builder()
            .username("admin")
            .name("Admin")
            .email("abc")
            .build();
    }

    public static final UserCreate missingEmail() {
        return UserCreateRequest.builder()
            .username("admin")
            .name("Admin")
            .build();
    }

    public static final UserCreate paddedWithWhitespaces() {
        return UserCreateRequest.builder()
            .username(" admin ")
            .name(" Admin ")
            .email(" email@somewhere.com ")
            .build();
    }

    public static final UserCreate valid() {
        return UserCreateRequest.builder()
            .username("admin")
            .name("Admin")
            .email("email@somewhere.com")
            .build();
    }

    public static final UserCreate valid(final String username, final String email) {
        return UserCreateRequest.builder()
            .username(username)
            .name("Admin")
            .email(email)
            .build();
    }

}
