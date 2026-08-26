
package com.bernardomg.security.domain.test.user.config.factory;

import java.time.Instant;

public final class UserTokenConstants {

    public static final Instant CREATION_DATE           = Instant.now();

    public static final Instant EXPIRATION_DATE         = Instant.now()
        .plusSeconds(3600);

    public static final Instant EXPIRATION_DATE_EXPIRED = Instant.now()
        .minusSeconds(1);

    public static final String  NAME                    = "Token name";

    public static final String  SCOPE                   = "password-reset";

    public static final String  TOKEN                   = "token-code";

    public static final String  USERNAME                = "username";

    private UserTokenConstants() {
        super();
    }

}
