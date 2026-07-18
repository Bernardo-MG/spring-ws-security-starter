
package com.bernardomg.security.login.domain.model;

import java.time.Instant;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

public record LoginRegister(String username, Boolean loggedIn, Instant date) {

    public LoginRegister(final String username, final Boolean loggedIn, final Instant date) {
        Objects.requireNonNull(username, "Username can't be null");
        Objects.requireNonNull(loggedIn, "Logged in flag can't be null");
        Objects.requireNonNull(date, "Date can't be null");

        this.username = StringUtils.trim(username);
        this.loggedIn = loggedIn;
        this.date = date;
    }

}
