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

package com.bernardomg.security.user.domain.event;

import java.util.Objects;

import com.bernardomg.event.domain.AbstractEvent;
import com.bernardomg.security.user.domain.model.User;

/**
 * User invitation event.
 */
public final class UserInvitationEvent extends AbstractEvent {

    private static final long serialVersionUID = 4486597593510214141L;

    private final String      token;

    private final User        user;

    public UserInvitationEvent(final Object source, final User user, final String token) {
        super(source);

        this.user = Objects.requireNonNull(user);
        this.token = Objects.requireNonNull(token);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final UserInvitationEvent other = (UserInvitationEvent) obj;
        return Objects.equals(user, other.user) && Objects.equals(token, other.token);
    }

    public final String getToken() {
        return token;
    }

    public final User getUser() {
        return user;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(user, token);
    }

    @Override
    public final String toString() {
        return "UserInvitationEvent [user=" + user + ", token=" + token + "]";
    }

}
