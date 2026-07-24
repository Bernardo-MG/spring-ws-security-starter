
package com.bernardomg.security.usecase.login.service;

import com.bernardomg.security.domain.user.model.User;

public interface UserAuthenticator {

    public User load(final String username, final String password);

}
