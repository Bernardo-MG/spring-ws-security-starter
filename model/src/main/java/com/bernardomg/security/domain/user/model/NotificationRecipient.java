
package com.bernardomg.security.domain.user.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * TODO: maybe should be in its own package
 */
public record NotificationRecipient(String username, String email, String name) implements Serializable {

    private static final long serialVersionUID = 1L;

    public NotificationRecipient {
        Objects.requireNonNull(username, "Received null username");
        Objects.requireNonNull(email, "Received null email");
    }

    public static NotificationRecipient from(final User user) {
        Objects.requireNonNull(user, "Received null user");

        return new NotificationRecipient(user.username(), user.email(), user.name());
    }

}
