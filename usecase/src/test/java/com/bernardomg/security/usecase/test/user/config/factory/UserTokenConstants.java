
package com.bernardomg.security.usecase.test.user.config.factory;

import java.time.Duration;
import java.time.Instant;

public final class UserTokenConstants {

    public static final Instant  CREATION_DATE   = Instant.now();

    public static final Instant  EXPIRATION_DATE = Instant.now()
        .plusSeconds(3600);

    public static final String   NAME            = "Token name";

    public static final String   SCOPE           = "password-reset";

    public static final String   TOKEN           = "token-code";

    public static final String   USERNAME        = "username";

    public static final Duration VALIDITY        = Duration.ofHours(1);

    private UserTokenConstants() {
        super();
    }

}
