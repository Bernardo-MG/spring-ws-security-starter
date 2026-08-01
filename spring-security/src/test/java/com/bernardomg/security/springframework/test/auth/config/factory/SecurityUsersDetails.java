
package com.bernardomg.security.springframework.test.auth.config.factory;

import java.util.List;

import com.bernardomg.security.springframework.test.user.config.factory.UserConstants;
import com.bernardomg.security.springframework.userdetails.SecurityUserDetails;

public final class SecurityUsersDetails {

    public static final SecurityUserDetails credentialsExpired() {
        return new SecurityUserDetails(UserConstants.ID, UserConstants.EMAIL, UserConstants.USERNAME,
            UserConstants.NAME, UserConstants.PASSWORD, true, true, false, true, List.of());
    }

    public static final SecurityUserDetails disabled() {
        return new SecurityUserDetails(UserConstants.ID, UserConstants.EMAIL, UserConstants.USERNAME,
            UserConstants.NAME, UserConstants.PASSWORD, false, true, true, true, List.of());
    }

    public static final SecurityUserDetails enabled() {
        return new SecurityUserDetails(UserConstants.ID, UserConstants.EMAIL, UserConstants.USERNAME,
            UserConstants.NAME, UserConstants.PASSWORD, true, true, true, true, List.of());
    }

    public static final SecurityUserDetails expired() {
        return new SecurityUserDetails(UserConstants.ID, UserConstants.EMAIL, UserConstants.USERNAME,
            UserConstants.NAME, UserConstants.PASSWORD, true, false, true, true, List.of());
    }

    public static final SecurityUserDetails locked() {
        return new SecurityUserDetails(UserConstants.ID, UserConstants.EMAIL, UserConstants.USERNAME,
            UserConstants.NAME, UserConstants.PASSWORD, true, true, true, false, List.of());
    }

    public static final SecurityUserDetails permission() {
        return new SecurityUserDetails(UserConstants.ID, UserConstants.EMAIL, UserConstants.USERNAME,
            UserConstants.NAME, UserConstants.PASSWORD, true, true, true, true,
            List.of(GrantedAuthorities.resourceCreate()));
    }

    private SecurityUsersDetails() {
        super();
    }

}
