
package com.bernardomg.security.login.event;

import java.util.Objects;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public final class LogInEvent extends ApplicationEvent {

    private static final long serialVersionUID = 4486597593510214141L;

    private final boolean     loggedIn;

    private final String      username;

    public LogInEvent(final Object source, final String user, final boolean logged) {
        super(source);

        username = Objects.requireNonNull(user);
        loggedIn = logged;
    }

}
