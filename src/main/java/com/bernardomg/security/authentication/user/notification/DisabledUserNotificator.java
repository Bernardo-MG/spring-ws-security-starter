
package com.bernardomg.security.authentication.user.notification;

public final class DisabledUserNotificator implements UserNotificator {

    public DisabledUserNotificator() {
        super();
    }

    @Override
    public final void sendUserRegisteredMessage(final String email, final String username, final String token) {}

}
