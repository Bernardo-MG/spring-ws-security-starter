
package com.bernardomg.security.login.domain.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(setterPrefix = "with")
public final class LoginRegister {

    public static final LoginRegister of(final String usnm, final boolean lgd, final LocalDateTime dt) {
        return LoginRegister.builder()
            .withUsername(usnm)
            .withLoggedIn(lgd)
            .withDate(dt)
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
