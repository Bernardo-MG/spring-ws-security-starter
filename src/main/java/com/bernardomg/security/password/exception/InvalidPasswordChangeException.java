
package com.bernardomg.security.password.exception;

import lombok.Getter;

@Getter
public final class InvalidPasswordChangeException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -1717035586281773602L;

    private final String      username;

    public InvalidPasswordChangeException(final String message, final String user) {
        super(message);

        username = user;
    }

}
