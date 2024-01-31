
package com.bernardomg.security.login.test.config.factory;

import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.login.domain.event.LogInEvent;

public final class LogInEvents {

    public static final LogInEvent loggedIn(final Object source) {
        return new LogInEvent(source, UserConstants.USERNAME, true);
    }

    public static final LogInEvent notLoggedIn(final Object source) {
        return new LogInEvent(source, UserConstants.USERNAME, false);
    }

    private LogInEvents() {
        super();
    }

}
