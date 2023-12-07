
package com.bernardomg.security.login.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoginRegister {

    /**
     * Logging attempt date.
     */
    private LocalDateTime date;

    /**
     * Id.
     */
    private Long          id;

    /**
     * Logged in flag.
     */
    private Boolean       loggedIn;

    /**
     * User name.
     */
    private String        username;

}
