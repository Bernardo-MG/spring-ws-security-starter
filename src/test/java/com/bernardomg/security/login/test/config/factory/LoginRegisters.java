
package com.bernardomg.security.login.test.config.factory;

import com.bernardomg.security.login.domain.model.LoginRegister;

public final class LoginRegisters {

    public static final LoginRegister loggedIn() {
        return new LoginRegister(LoginConstants.USERNAME,true,LoginConstants.DATE);
    }

    public static final LoginRegister notLoggedIn() {
        return new LoginRegister(LoginConstants.USERNAME,false,LoginConstants.DATE);
    }

    private LoginRegisters() {
        super();
    }

}
