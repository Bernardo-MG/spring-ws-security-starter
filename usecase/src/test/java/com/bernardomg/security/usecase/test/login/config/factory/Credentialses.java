
package com.bernardomg.security.usecase.test.login.config.factory;

import com.bernardomg.security.domain.login.model.Credentials;
import com.bernardomg.security.usecase.test.user.config.factory.UserConstants;

public class Credentialses {

    public static final Credentials valid() {
        return new Credentials(UserConstants.USERNAME, UserConstants.PASSWORD);
    }

    private Credentialses() {
        super();
    }

}
