
package com.bernardomg.security.authentication.user.test.util.model;

import com.bernardomg.security.authentication.user.model.query.UserUpdateRequest;

public final class UserUpdateRequests {

    public static final UserUpdateRequest disabled() {
        return UserUpdateRequest.builder()
            .id(1L)
            .name("Admin")
            .email("email@somewhere.com")
            .enabled(false)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdateRequest emailChange() {
        return UserUpdateRequest.builder()
            .id(1L)
            .name("Admin")
            .email("email2@somewhere.com")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdateRequest emailChangeUpperCase() {
        return UserUpdateRequest.builder()
            .id(1L)
            .name("Admin")
            .email("EMAIL2@SOMEWHERE.COM")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdateRequest enabled() {
        return UserUpdateRequest.builder()
            .id(1L)
            .name("Admin")
            .email("email@somewhere.com")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdateRequest invalidEmail() {
        return UserUpdateRequest.builder()
            .id(1L)
            .name("Admin")
            .email("abc")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdateRequest nameChange() {
        return UserUpdateRequest.builder()
            .id(1L)
            .name("Admin2")
            .email("email@somewhere.com")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdateRequest noEmail() {
        return UserUpdateRequest.builder()
            .id(1L)
            .name("Admin")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdateRequest noEnabled() {
        return UserUpdateRequest.builder()
            .id(1L)
            .name("Admin")
            .email("email@somewhere.com")
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdateRequest noId() {
        return UserUpdateRequest.builder()
            .name("Admin")
            .email("email@somewhere.com")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdateRequest paddedWithWhitespaces() {
        return UserUpdateRequest.builder()
            .id(1L)
            .name(" Admin ")
            .email(" email2@somewhere.com ")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdateRequest passwordExpired() {
        return UserUpdateRequest.builder()
            .id(1L)
            .name("Admin")
            .email("email@somewhere.com")
            .enabled(true)
            .passwordExpired(true)
            .build();
    }

}
