
package com.bernardomg.security.authentication.user.exception;

public final class UserExpiredException extends RuntimeException {

    private static final long serialVersionUID = -7226031317959127168L;

    public UserExpiredException(final String username) {
        super(String.format("User %s is expired", username));
    }

}
