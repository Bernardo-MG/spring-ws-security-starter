
package com.bernardomg.security.login.domain.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(setterPrefix = "with")
public final class LoginRegister {

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
