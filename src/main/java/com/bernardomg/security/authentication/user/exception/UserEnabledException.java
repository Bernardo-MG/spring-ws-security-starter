
package com.bernardomg.security.authentication.user.exception;

public final class UserEnabledException extends RuntimeException {

    private static final long serialVersionUID = -7226031317959127168L;

    public UserEnabledException(final String username) {
        super(String.format("User %s is enabled", username));
    }

}
