
package com.bernardomg.security.jwt.test.configuration;

import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;

import javax.crypto.SecretKey;

import io.jsonwebtoken.security.Keys;

public final class Tokens {

    public static final String    ALTERNATIVE_SCOPE = "scope2";

    public static final String    ALTERNATIVE_TOKEN = "bd656aaf-0c18-4178-bcdf-71ccb7f320fb";

    public static final String    AUDIENCE          = "audience";

    public static final Instant   EXPIRED_DATE      = LocalDate.of(2020, Month.FEBRUARY, 1)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant();

    public static final Instant   ISSUED_AT         = LocalDate.of(2020, Month.FEBRUARY, 1)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant();

    public static final String    ISSUER            = "issuer";

    public static final SecretKey KEY               = Keys.hmacShaKeyFor(
        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
            .getBytes(Charset.forName("UTF-8")));

    public static final Instant   NEXT_MONTH_DATE   = LocalDate.now()
        .plusMonths(1)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant();

    public static final Instant   NOT_BEFORE        = LocalDate.of(2020, Month.FEBRUARY, 1)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant();

    public static final String    SCOPE             = "scope";

    public static final String    SUBJECT           = "subject";

    public static final String    TOKEN             = "bd656aaf-0c18-4178-bcdf-71ccb7f320fa";

    private Tokens() {
        super();
    }

}
