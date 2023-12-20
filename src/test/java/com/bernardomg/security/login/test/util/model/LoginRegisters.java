
package com.bernardomg.security.login.test.util.model;

import java.time.LocalDateTime;
import java.time.Month;

import com.bernardomg.security.login.model.LoginRegister;

public final class LoginRegisters {

    public static final LocalDateTime DATE     = LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0);

    public static final String        USERNAME = "username";

    public static final LoginRegister loggedIn() {
        return LoginRegister.builder()
            .withUsername(USERNAME)
            .withLoggedIn(true)
            .withDate(DATE)
            .build();
    }

    private LoginRegisters() {
        super();
    }

}
