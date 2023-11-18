
package com.bernardomg.security.authentication.password.notification;

public final class DisabledPasswordNotificator implements PasswordNotificator {

    public DisabledPasswordNotificator() {
        super();
    }

    @Override
    public final void sendPasswordRecoveryMessage(final String email, final String username, final String token) {}

}
