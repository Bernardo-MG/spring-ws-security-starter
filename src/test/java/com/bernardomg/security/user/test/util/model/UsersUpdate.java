
package com.bernardomg.security.user.test.util.model;

import com.bernardomg.security.user.model.request.UserUpdate;
import com.bernardomg.security.user.model.request.ValidatedUserUpdate;

public final class UsersUpdate {

    public static final UserUpdate disabled() {
        return ValidatedUserUpdate.builder()
            .id(1L)
            .name("Admin")
            .email("email@somewhere.com")
            .enabled(false)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdate emailChange() {
        return ValidatedUserUpdate.builder()
            .id(1L)
            .name("Admin")
            .email("email2@somewhere.com")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdate emailChangeUpperCase() {
        return ValidatedUserUpdate.builder()
            .id(1L)
            .name("Admin")
            .email("EMAIL2@SOMEWHERE.COM")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdate enabled() {
        return ValidatedUserUpdate.builder()
            .id(1L)
            .name("Admin")
            .email("email@somewhere.com")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdate invalidEmail() {
        return ValidatedUserUpdate.builder()
            .id(1L)
            .name("Admin")
            .email("abc")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdate nameChange() {
        return ValidatedUserUpdate.builder()
            .id(1L)
            .name("Admin2")
            .email("email@somewhere.com")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdate noEmail() {
        return ValidatedUserUpdate.builder()
            .id(1L)
            .name("Admin")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdate noEnabled() {
        return ValidatedUserUpdate.builder()
            .id(1L)
            .name("Admin")
            .email("email@somewhere.com")
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdate noId() {
        return ValidatedUserUpdate.builder()
            .name("Admin")
            .email("email@somewhere.com")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdate paddedWithWhitespaces() {
        return ValidatedUserUpdate.builder()
            .id(1L)
            .name(" Admin ")
            .email(" email2@somewhere.com ")
            .enabled(true)
            .passwordExpired(false)
            .build();
    }

    public static final UserUpdate passwordExpired() {
        return ValidatedUserUpdate.builder()
            .id(1L)
            .name("Admin")
            .email("email@somewhere.com")
            .enabled(true)
            .passwordExpired(true)
            .build();
    }

}
