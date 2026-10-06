
package com.bernardomg.security.domain.login.event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

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

    public LogInEvent(final UUID id, final String source, final int schemaVersion, final Instant timestamp,
            final String username, final boolean loggedIn) {
        super(id, source, TYPE, schemaVersion, timestamp);

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
