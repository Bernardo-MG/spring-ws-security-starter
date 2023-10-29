
package com.bernardomg.security.user.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class ValidatedUserQuery implements UserQuery {

    /**
     * User email.
     */
    private String  email;

    /**
     * User enabled flag.
     */
    private Boolean enabled;

    /**
     * User expired flag.
     */
    private Boolean expired;

    /**
     * User locked flag.
     */
    private Boolean locked;

    /**
     * User name.
     */
    private String  name;

    /**
     * Password expired flag.
     */
    private Boolean passwordExpired;

    /**
     * User username.
     */
    private String  username;

}
