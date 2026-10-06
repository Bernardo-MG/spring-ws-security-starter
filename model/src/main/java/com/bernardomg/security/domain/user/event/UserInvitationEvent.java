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

package com.bernardomg.security.domain.user.event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.bernardomg.event.domain.AbstractEvent;
import com.bernardomg.security.domain.user.model.NotificationRecipient;

public final class UserInvitationEvent extends AbstractEvent {

    public static final String          TYPE             = "security.user-invitation.requested";

    private static final long           serialVersionUID = 9158281731299154307L;

    private final NotificationRecipient recipient;

    private final String                token;

    public UserInvitationEvent(final String source, final NotificationRecipient recipient, final String token) {
        super(source, TYPE, 1);
        this.recipient = Objects.requireNonNull(recipient);
        this.token = Objects.requireNonNull(token);
    }

    public UserInvitationEvent(final UUID id, final String source, final int schemaVersion, final Instant timestamp,
            final NotificationRecipient recipient, final String token) {
        super(id, source, TYPE, schemaVersion, timestamp);
        if (schemaVersion < 1) {
            throw new IllegalArgumentException("schemaVersion must be positive");
        }
        this.recipient = Objects.requireNonNull(recipient);
        this.token = Objects.requireNonNull(token);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof final UserInvitationEvent other)) {
            return false;
        }
        return Objects.equals(getSource(), other.getSource()) && Objects.equals(getId(), other.getId());
    }

    public NotificationRecipient getRecipient() {
        return recipient;
    }

    public String getToken() {
        return token;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "UserInvitationEvent [id=" + getId() + ", username=" + recipient.username() + "]";
    }
}
