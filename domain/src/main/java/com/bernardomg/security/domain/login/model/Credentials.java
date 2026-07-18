
package com.bernardomg.security.domain.login.model;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * User credentials.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public record Credentials(String username, String password) {

    public Credentials(final String username, final String password) {
        Objects.requireNonNull(username, "Username can't be null");
        Objects.requireNonNull(password, "Password can't be null");

        this.username = StringUtils.trim(username);
        this.password = StringUtils.trim(password);
    }

}
