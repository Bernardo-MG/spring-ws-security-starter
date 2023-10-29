
package com.bernardomg.security.authentication.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class DtoUser implements User {

    /**
     * User email.
     */
    private final String  email;

    /**
     * User enabled flag.
     */
    private final Boolean enabled;

    /**
     * User expired flag.
     */
    private final Boolean expired;

    /**
     * User id.
     */
    private final Long    id;

    /**
     * User locked flag.
     */
    private final Boolean locked;

    /**
     * User name.
     */
    private final String  name;

    /**
     * Password expired flag.
     */
    private final Boolean passwordExpired;

    /**
     * User username.
     */
    private final String  username;

}
