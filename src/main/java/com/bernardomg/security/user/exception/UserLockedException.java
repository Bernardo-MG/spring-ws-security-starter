
package com.bernardomg.security.user.exception;

public final class UserLockedException extends RuntimeException {

    private static final long serialVersionUID = -7226031317959127168L;

    public UserLockedException(final String username) {
        super(String.format("User %s is locked", username));
    }

}
