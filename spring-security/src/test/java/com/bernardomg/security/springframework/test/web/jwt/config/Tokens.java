
package com.bernardomg.security.springframework.test.web.jwt.config;

import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;

import javax.crypto.SecretKey;

import io.jsonwebtoken.security.Keys;

public final class Tokens {

    public static final String    ALTERNATIVE_SCOPE = "scope2";

    public static final String    ALTERNATIVE_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdWJqZWN0In0.8fhjRsjPswScvhcPnN56SHmpKtqO53EpCmCIZg0SDp_sjeaMEAV4GfqPK5spRYdSZPKfdjz1FxvF7re9Sc6nZg";

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

    public static final String    TOKEN             = "eyJhbGciOiJIUzUxMiJ9.eyJhdWQiOlsiYXVkaWVuY2UiXX0.MC9oB7dYxYp6yrzZwKazSZ484NUwTQrs_sgAMeBdVPMpF0aU83GRF3fLD8FhiamG5f0yWH6LxsWUUARVqdA6pA";

    private Tokens() {
        super();
    }

}
