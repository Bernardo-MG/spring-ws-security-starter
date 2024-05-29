
package com.bernardomg.security.login.domain.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(setterPrefix = "with")
public final class LoginRegister {

    public static final LoginRegister of(final String usnm, final LocalDateTime dt, final boolean lgd) {
        return LoginRegister.builder()
            .withUsername(usnm)
            .withDate(dt)
            .withLoggedIn(lgd)
            .build();
    }

    /**
     * Logging attempt date.
     */
    private final LocalDateTime date;

    /**
     * Logged in flag.
     */
    private final boolean       loggedIn;

    /**
     * User name.
     */
    private final String        username;

}
