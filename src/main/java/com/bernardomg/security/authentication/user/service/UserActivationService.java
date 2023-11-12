
package com.bernardomg.security.authentication.user.service;

import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.model.query.UserCreate;
import com.bernardomg.security.user.token.model.UserTokenStatus;

public interface UserActivationService {

    public User activateNewUser(final String token, final String password);

    /**
     * Persists the received user.
     *
     * @param user
     *            user to persist
     * @return the persisted user
     */
    public User registerNewUser(final UserCreate user);

    /**
     * Validate a user registration token.
     *
     * @param token
     *            token to validate
     * @return token status
     */
    public UserTokenStatus validateToken(final String token);

}
