
package com.bernardomg.test.config.factory;

import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;

public final class SecurityUsers {

    public static final UserDetails disabled() {
        return new User(UserConstants.USERNAME, UserConstants.PASSWORD, false, true, true, true, List.of());
    }

    public static final UserDetails enabled() {
        return new User(UserConstants.USERNAME, UserConstants.PASSWORD, true, true, true, true, List.of());
    }

    public static final UserDetails expired() {
        return new User(UserConstants.USERNAME, UserConstants.PASSWORD, true, false, true, true, List.of());
    }

    public static final UserDetails locked() {
        return new User(UserConstants.USERNAME, UserConstants.PASSWORD, true, true, true, false, List.of());
    }

    private SecurityUsers() {
        super();
    }

}
