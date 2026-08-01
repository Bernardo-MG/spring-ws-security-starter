
package com.bernardomg.security.springframework.test.auth.config.factory;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.bernardomg.security.springframework.test.user.config.factory.UserConstants;
import com.bernardomg.security.springframework.userdetails.SecurityUserDetails;

public final class SecurityUsersDetails {

    public static final UserDetails credentialsExpired() {
        return new SecurityUserDetails(UserConstants.ID, UserConstants.EMAIL, UserConstants.USERNAME,
            UserConstants.NAME, UserConstants.PASSWORD, true, true, false, true, List.of());
    }

    public static final UserDetails disabled() {
        return new SecurityUserDetails(UserConstants.ID, UserConstants.EMAIL, UserConstants.USERNAME,
            UserConstants.NAME, UserConstants.PASSWORD, false, true, true, true, List.of());
    }

    public static final UserDetails enabled() {
        return new SecurityUserDetails(UserConstants.ID, UserConstants.EMAIL, UserConstants.USERNAME,
            UserConstants.NAME, UserConstants.PASSWORD, true, true, true, true, List.of());
    }

    public static final UserDetails expired() {
        return new SecurityUserDetails(UserConstants.ID, UserConstants.EMAIL, UserConstants.USERNAME,
            UserConstants.NAME, UserConstants.PASSWORD, true, false, true, true, List.of());
    }

    public static final UserDetails locked() {
        return new SecurityUserDetails(UserConstants.ID, UserConstants.EMAIL, UserConstants.USERNAME,
            UserConstants.NAME, UserConstants.PASSWORD, true, true, true, false, List.of());
    }

    public static final UserDetails permission() {
        return new SecurityUserDetails(UserConstants.ID, UserConstants.EMAIL, UserConstants.USERNAME,
            UserConstants.NAME, UserConstants.PASSWORD, true, true, true, true,
            List.of(GrantedAuthorities.resourceCreate()));
    }

    private SecurityUsersDetails() {
        super();
    }

}
