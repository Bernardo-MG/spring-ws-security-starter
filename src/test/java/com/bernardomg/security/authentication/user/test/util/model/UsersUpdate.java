
package com.bernardomg.security.authentication.user.test.util.model;

import com.bernardomg.security.authentication.user.model.query.UserUpdate;
import com.bernardomg.security.authentication.user.model.query.UserUpdateRequest;

public final class UsersUpdate {

    public static final UserUpdate disabled() {
        return UserUpdateRequest.builder()
            .id(1L)
            .name("Admin")
            .email("email@somewhere.com")
            .enabled(false)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdate emailChange() {
        return UserUpdateRequest.builder()
            .id(1L)
            .name("Admin")
            .email("email2@somewhere.com")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdate emailChangeUpperCase() {
        return UserUpdateRequest.builder()
            .id(1L)
            .name("Admin")
            .email("EMAIL2@SOMEWHERE.COM")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdate enabled() {
        return UserUpdateRequest.builder()
            .id(1L)
            .name("Admin")
            .email("email@somewhere.com")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdate invalidEmail() {
        return UserUpdateRequest.builder()
            .id(1L)
            .name("Admin")
            .email("abc")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdate nameChange() {
        return UserUpdateRequest.builder()
            .id(1L)
            .name("Admin2")
            .email("email@somewhere.com")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdate noEmail() {
        return UserUpdateRequest.builder()
            .id(1L)
            .name("Admin")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdate noEnabled() {
        return UserUpdateRequest.builder()
            .id(1L)
            .name("Admin")
            .email("email@somewhere.com")
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdate noId() {
        return UserUpdateRequest.builder()
            .name("Admin")
            .email("email@somewhere.com")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdate paddedWithWhitespaces() {
        return UserUpdateRequest.builder()
            .id(1L)
            .name(" Admin ")
            .email(" email2@somewhere.com ")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdate passwordExpired() {
        return UserUpdateRequest.builder()
            .id(1L)
            .name("Admin")
            .email("email@somewhere.com")
            .enabled(true)
            .passwordExpired(true)
            .build();
    }

}
