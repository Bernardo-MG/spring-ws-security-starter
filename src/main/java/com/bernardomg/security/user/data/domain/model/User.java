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

package com.bernardomg.security.user.data.domain.model;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.user.data.domain.exception.EnabledUserException;
import com.bernardomg.security.user.data.domain.exception.ExpiredUserException;
import com.bernardomg.security.user.data.domain.exception.LockedUserException;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * Representation of a user.
 * <p>
 * FIXME: this should be immutable
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Builder(setterPrefix = "with")
@Slf4j
public record User(String email, String username, String name, boolean enabled, boolean expired, boolean locked,
        boolean passwordExpired, Collection<Role> roles) {

    public User(final String email, final String username, final String name, final boolean enabled,
            final boolean expired, final boolean locked, final boolean passwordExpired, final Collection<Role> roles) {
        if (Objects.nonNull(name)) {
            this.name = name.trim();
        } else {
            this.name = null;
        }

        if (Objects.nonNull(username)) {
            this.username = username.trim()
                .toLowerCase();
        } else {
            this.username = null;
        }

        if (Objects.nonNull(email)) {
            this.email = email.trim()
                .toLowerCase();
        } else {
            this.email = null;
        }

        this.enabled = enabled;
        this.expired = expired;
        this.locked = locked;
        this.passwordExpired = passwordExpired;
        this.roles = roles;
    }

    public static final User newUser(final String username, final String email, final String name) {
        return new User(email, username, name, false, false, false, true, List.of());
    }

    public final void checkStatus() {
        // TODO: Send a single exception with all the cases
        // TODO: Test
        if (expired) {
            log.error("User {} is expired", username);
            throw new ExpiredUserException(username);
        }
        if (locked) {
            log.error("User {} is locked", username);
            throw new LockedUserException(username);
        }
        if (enabled) {
            log.error("User {} is already enabled", username);
            throw new EnabledUserException(username);
        }
    }

}
