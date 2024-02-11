
package com.bernardomg.security.authorization.token.test.config.factory;

import java.time.LocalDateTime;
import java.time.Month;

public final class UserTokenConstants {

    public static final LocalDateTime DATE             = LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0);

    public static final LocalDateTime DATE_FUTURE      = LocalDateTime.of(2030, Month.FEBRUARY, 1, 0, 0);

    public static final LocalDateTime DATE_MORE_FUTURE = LocalDateTime.of(2040, Month.FEBRUARY, 1, 0, 0);

    public static final LocalDateTime DATE_PAST        = LocalDateTime.of(2000, Month.FEBRUARY, 1, 0, 0);

    public static final LocalDateTime DATE_YESTERDAY   = LocalDateTime.now()
        .minusDays(1);

    private UserTokenConstants() {
        super();
    }

}
