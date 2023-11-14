
package com.bernardomg.security.authentication.user.test.util.model;

import com.bernardomg.security.authentication.user.model.ImmutableUser;
import com.bernardomg.security.authentication.user.model.User;

public final class Users {

    public static String ALTERNATIVE_EMAIL = "mail2@somewhere.com";

    public static String EMAIL             = "mail@somewhere.com";

    public static String ENCODED_PASSWORD  = "$2a$04$gV.k/KKIqr3oPySzs..bx.8absYRTpNe8AbHmPP90.ErW0ICGOsVW";

    public static String NAME              = "Admin";

    public static String PASSWORD          = "1234";

    public static String USERNAME          = "admin";

    public static final User enabled() {
        return ImmutableUser.builder()
            .id(1L)
            .name(Users.NAME)
            .username(Users.USERNAME)
            .email(Users.EMAIL)
            .enabled(true)
            .expired(false)
            .passwordExpired(false)
            .locked(false)
            .build();
    }

}
