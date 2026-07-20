
package com.bernardomg.security.usecase.test.login.config.factory;

import com.bernardomg.security.domain.login.model.LoginRegister;

public final class LoginRegisters {

    public static final LoginRegister loggedIn() {
        return new LoginRegister(LoginConstants.USERNAME, true, LoginConstants.DATE);
    }

    public static final LoginRegister notLoggedIn() {
        return new LoginRegister(LoginConstants.USERNAME, false, LoginConstants.DATE);
    }

    private LoginRegisters() {
        super();
    }

}
