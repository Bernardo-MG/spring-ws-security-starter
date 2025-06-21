
package com.bernardomg.security.login.test.config.factory;

import com.bernardomg.security.login.adapter.inbound.jpa.model.LoginRegisterEntity;

public final class LoginRegisterEntities {

    public static final LoginRegisterEntity loggedIn() {
        final LoginRegisterEntity entity;

        entity = new LoginRegisterEntity();
        entity.setId(1L);
        entity.setUsername(LoginConstants.USERNAME);
        entity.setLoggedIn(true);
        entity.setDate(LoginConstants.DATE);

        return entity;
    }

    public static final LoginRegisterEntity notLoggedIn() {
        final LoginRegisterEntity entity;

        entity = new LoginRegisterEntity();
        entity.setId(1L);
        entity.setUsername(LoginConstants.USERNAME);
        entity.setLoggedIn(false);
        entity.setDate(LoginConstants.DATE);

        return entity;
    }

    private LoginRegisterEntities() {
        super();
    }

}
