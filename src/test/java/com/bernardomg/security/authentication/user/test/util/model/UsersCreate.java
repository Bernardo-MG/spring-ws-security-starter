
package com.bernardomg.security.authentication.user.test.util.model;

import com.bernardomg.security.authentication.user.model.query.UserCreate;
import com.bernardomg.security.authentication.user.model.query.ValidatedUserCreate;

public final class UsersCreate {

    public static final UserCreate alternative() {
        return ValidatedUserCreate.builder()
            .username("user")
            .name("User")
            .email("email2@somewhere.com")
            .build();
    }

    public static final UserCreate invalidEmail() {
        return ValidatedUserCreate.builder()
            .username("admin")
            .name("Admin")
            .email("abc")
            .build();
    }

    public static final UserCreate missingEmail() {
        return ValidatedUserCreate.builder()
            .username("admin")
            .name("Admin")
            .build();
    }

    public static final UserCreate paddedWithWhitespaces() {
        return ValidatedUserCreate.builder()
            .username(" admin ")
            .name(" Admin ")
            .email(" email@somewhere.com ")
            .build();
    }

    public static final UserCreate valid() {
        return ValidatedUserCreate.builder()
            .username("admin")
            .name("Admin")
            .email("email@somewhere.com")
            .build();
    }

    public static final UserCreate valid(final String username, final String email) {
        return ValidatedUserCreate.builder()
            .username(username)
            .name("Admin")
            .email(email)
            .build();
    }

}
