
package com.bernardomg.security.domain.login.exception;

public final class InvalidCredentialsException extends RuntimeException {

    private static final long serialVersionUID = -8734501650879205354L;

    public InvalidCredentialsException() {
        super("Invalid username or credentials");
    }

    public InvalidCredentialsException(final Throwable cause) {
        super("Invalid username or credentials", cause);
    }
}
