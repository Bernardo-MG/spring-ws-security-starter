
package com.bernardomg.security.authentication.user.test.config.factory;

import com.bernardomg.security.authentication.user.model.NewUser;

public final class NewUsers {

    public static final NewUser invalidEmail() {
        return NewUser.builder()
            .withUsername(UserConstants.NAME)
            .withName(UserConstants.NAME)
            .withEmail("abc")
            .build();
    }

    public static final NewUser missingEmail() {
        return NewUser.builder()
            .withUsername(UserConstants.NAME)
            .withName(UserConstants.NAME)
            .build();
    }

    public static final NewUser valid() {
        return NewUser.builder()
            .withUsername(UserConstants.NAME)
            .withName(UserConstants.NAME)
            .withEmail(UserConstants.EMAIL)
            .build();
    }

}
