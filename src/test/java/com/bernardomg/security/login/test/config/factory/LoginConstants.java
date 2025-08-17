
package com.bernardomg.security.login.test.config.factory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;

public final class LoginConstants {

    public static final Instant DATE     = LocalDate.of(2020, Month.JANUARY, 1)
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant();

    public static final String  USERNAME = "username";

    private LoginConstants() {
        super();
    }

}
