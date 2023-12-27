
package com.bernardomg.security.login.test.config.factory;

import java.time.LocalDateTime;
import java.time.Month;

public final class LoginConstants {

    public static final LocalDateTime DATE     = LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0);

    public static final String        USERNAME = "username";

    private LoginConstants() {
        super();
    }

}
