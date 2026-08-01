
package com.bernardomg.security.usecase.login.authentication;

import com.bernardomg.security.domain.login.model.Credentials;
import com.bernardomg.security.usecase.login.domain.LoginUser;

public interface UserAuthenticator {

    public LoginUser load(final Credentials credentials);

}
