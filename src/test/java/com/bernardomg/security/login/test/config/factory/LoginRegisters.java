
package com.bernardomg.security.login.test.config.factory;

import com.bernardomg.security.login.domain.model.LoginRegister;

public final class LoginRegisters {

    public static final LoginRegister loggedIn() {
        return LoginRegister.builder()
            .withUsername(LoginConstants.USERNAME)
            .withLoggedIn(true)
            .withDate(LoginConstants.DATE)
            .build();
    }

    private LoginRegisters() {
        super();
    }

}
