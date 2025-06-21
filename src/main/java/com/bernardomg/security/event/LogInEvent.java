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

package com.bernardomg.security.event;

import java.util.Objects;

import org.springframework.context.ApplicationEvent;

/**
 * Log in attempt event. It is created no matter if the attempt was succesful or not.
 */
public final class LogInEvent extends ApplicationEvent {

    private static final long serialVersionUID = 4486597593510214141L;

    /**
     * Logged in successful or not flag.
     */
    private final boolean     loggedIn;

    /**
     * Username which attempted the log in.
     */
    private final String      username;

    public LogInEvent(final Object source, final String user, final boolean logged) {
        super(source);

        username = Objects.requireNonNull(user);
        loggedIn = logged;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final LogInEvent that) || !super.equals(o)) {
            return false;
        }

        return (loggedIn == that.loggedIn) && Objects.equals(username, that.username)
                && Objects.equals(getSource(), that.getSource());
    }

    public String getUsername() {
        return username;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, loggedIn);
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

}
