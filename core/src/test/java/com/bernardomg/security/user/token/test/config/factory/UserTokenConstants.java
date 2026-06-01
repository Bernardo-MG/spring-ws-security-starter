
package com.bernardomg.security.user.token.test.config.factory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;

public final class UserTokenConstants {

    public static final Instant DATE             = LocalDate.of(2020, Month.FEBRUARY, 1)
        .atStartOfDay()
        .toInstant(ZoneOffset.UTC);

    public static final Instant DATE_FUTURE      = LocalDate.of(2030, Month.FEBRUARY, 1)
        .atStartOfDay()
        .toInstant(ZoneOffset.UTC);

    public static final Instant DATE_MORE_FUTURE = LocalDate.of(2040, Month.FEBRUARY, 1)
        .atStartOfDay()
        .toInstant(ZoneOffset.UTC);

    public static final Instant DATE_PAST        = LocalDate.of(2000, Month.FEBRUARY, 1)
        .atStartOfDay()
        .toInstant(ZoneOffset.UTC);

    public static final Instant DATE_YESTERDAY   = LocalDate.now()
        .minusDays(1)
        .atStartOfDay()
        .toInstant(ZoneOffset.UTC);

    private UserTokenConstants() {
        super();
    }

}
