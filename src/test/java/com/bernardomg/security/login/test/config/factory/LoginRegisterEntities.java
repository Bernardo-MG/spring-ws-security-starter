
package com.bernardomg.security.login.test.config.factory;

import com.bernardomg.security.login.adapter.inbound.jpa.model.LoginRegisterEntity;

public final class LoginRegisterEntities {

    public static final LoginRegisterEntity loggedIn() {
        return LoginRegisterEntity.builder()
            .withId(1L)
            .withUsername(LoginConstants.USERNAME)
            .withLoggedIn(true)
            .withDate(LoginConstants.DATE)
            .build();
    }

    public static final LoginRegisterEntity notLoggedIn() {
        return LoginRegisterEntity.builder()
            .withId(1L)
            .withUsername(LoginConstants.USERNAME)
            .withLoggedIn(false)
            .withDate(LoginConstants.DATE)
            .build();
    }

    private LoginRegisterEntities() {
        super();
    }

}
