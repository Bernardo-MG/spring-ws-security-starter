
package com.bernardomg.security.login.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class LoginRegister {

    /**
     * Logging attempt date.
     */
    @NonNull
    private final LocalDateTime date;

    /**
     * Id.
     */
    private final long          id;

    /**
     * Logged in flag.
     */
    private final boolean       loggedIn;

    /**
     * User name.
     */
    @NonNull
    private final String        username;

}
