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

package com.bernardomg.security.domain.login.event;

import java.util.Objects;

import com.bernardomg.event.domain.AbstractEvent;

public final class LogInEvent extends AbstractEvent {

    public static final String TYPE             = "security.login.attempted";

    private static final long  serialVersionUID = -5605953607231205607L;

    private final boolean      loggedIn;

    private final String       username;

    public LogInEvent(final String source, final String username, final boolean loggedIn) {
        super(source, TYPE, 1);
        this.username = Objects.requireNonNull(username);
        this.loggedIn = loggedIn;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof final LogInEvent other)) {
            return false;
        }
        return Objects.equals(getSource(), other.getSource()) && Objects.equals(getId(), other.getId());
    }

    public final String getUsername() {
        return username;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getId());
    }

    public final boolean isLoggedIn() {
        return loggedIn;
    }

    @Override
    public final String toString() {
        return "LogInEvent [id=" + getId() + ", username=" + username + ", loggedIn=" + loggedIn + "]";
    }
}
