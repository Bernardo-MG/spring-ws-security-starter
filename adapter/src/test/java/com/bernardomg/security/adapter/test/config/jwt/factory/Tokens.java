
package com.bernardomg.security.adapter.test.config.jwt.factory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;

public final class Tokens {

    public static final String  ALTERNATIVE_SCOPE = "scope2";

    public static final String  ALTERNATIVE_TOKEN = "bd656aaf-0c18-4178-bcdf-71ccb7f320fb";

    public static final Instant CREATION_DATE     = LocalDate.of(2020, Month.FEBRUARY, 1)
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant();

    public static final Instant EXPIRATION        = LocalDate.of(2030, Month.FEBRUARY, 1)
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant();

    public static final Instant EXPIRED           = LocalDate.of(2000, Month.FEBRUARY, 1)
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant();

    public static final String  SCOPE             = "scope";

    public static final String  TOKEN             = "bd656aaf-0c18-4178-bcdf-71ccb7f320fa";

    private Tokens() {
        super();
    }

}
