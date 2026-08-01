
package com.bernardomg.security.usecase.login.authentication;

import com.bernardomg.security.domain.login.model.Credentials;
import com.bernardomg.security.usecase.login.domain.LoginUser;

/**
 * Authenticates a user for the login process.
 */
public interface LoginUserAuthenticator {

    /**
     * Authenticates a user.
     *
     * @param credentials
     *            user credentials
     * @return authenticated user
     */
    public LoginUser authenticate(final Credentials credentials);

}
