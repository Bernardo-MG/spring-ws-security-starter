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

package com.bernardomg.security.domain.event.model;

import com.bernardomg.security.domain.login.event.LogInEvent;
import com.bernardomg.security.domain.password.reset.event.PasswordResetEvent;
import com.bernardomg.security.domain.user.event.UserInvitationEvent;
import com.bernardomg.security.domain.user.model.NotificationRecipient;
import com.bernardomg.security.domain.user.model.User;

public final class SecurityEventFactory {

    private static final String source = "com.bernardomg.security";

    public static final LogInEvent loginAttempt(final String username, final boolean loggedIn) {
        return new LogInEvent(source, username, loggedIn);
    }

    public static final PasswordResetEvent passwordReset(final User user, final String token) {
        return new PasswordResetEvent(source, NotificationRecipient.from(user), token);
    }

    public static final UserInvitationEvent userInvitation(final User user, final String token) {
        return new UserInvitationEvent(source, NotificationRecipient.from(user), token);
    }

    private SecurityEventFactory() {
        super();
    }
}
