/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023-2025 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bernardomg.security.user.domain.model;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.user.domain.exception.EnabledUserException;
import com.bernardomg.security.user.domain.exception.ExpiredUserException;
import com.bernardomg.security.user.domain.exception.LockedUserException;

/**
 * Representation of a user.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public record User(String email, String username, String name, boolean enabled, boolean notExpired, boolean notLocked,
        boolean passwordNotExpired, Collection<Role> roles) {

    /**
     * Logger for the class.
     */
    private static final Logger log = LoggerFactory.getLogger(User.class);

    public User(final String email, final String username, final String name, final boolean enabled,
            final boolean notExpired, final boolean notLocked, final boolean passwordNotExpired,
            final Collection<Role> roles) {
        if (Objects.nonNull(name)) {
            this.name = name.trim();
        } else {
            this.name = null;
        }

        if (Objects.nonNull(username)) {
            this.username = username.trim()
                .toLowerCase(Locale.getDefault());
        } else {
            this.username = null;
        }

        if (Objects.nonNull(email)) {
            this.email = email.trim()
                .toLowerCase(Locale.getDefault());
        } else {
            this.email = null;
        }

        this.enabled = enabled;
        this.notExpired = notExpired;
        this.notLocked = notLocked;
        this.passwordNotExpired = passwordNotExpired;
        this.roles = roles;
    }

    public static final User newUser(final String username, final String email, final String name) {
        return new User(email, username, name, false, true, true, false, List.of());
    }

    public static final User newUser(final String username, final String email, final String name,
            final Collection<Role> roles) {
        return new User(email, username, name, false, true, true, false, roles);
    }

    public final void checkStatus() {
        // TODO: Send a single exception with all the cases
        // TODO: Test
        if (!notExpired) {
            log.error("User {} is expired", username);
            throw new ExpiredUserException(username);
        }
        if (!notLocked) {
            log.error("User {} is locked", username);
            throw new LockedUserException(username);
        }
        if (enabled) {
            log.error("User {} is already enabled", username);
            throw new EnabledUserException(username);
        }
    }

}
