
package com.bernardomg.security.user.exception;

public final class UserDisabledException extends RuntimeException {

    private static final long serialVersionUID = -7226031317959127168L;

    public UserDisabledException(final String username) {
        super(String.format("User %s is disabled", username));
    }

}
