
package com.bernardomg.security.user.token.exception;

import java.io.Serializable;

import com.bernardomg.exception.MissingIdException;

public final class MissingTokenException extends MissingIdException {

    private static final long serialVersionUID = 2786821546505029631L;

    public MissingTokenException(final Serializable id) {
        super("userToken", id);
    }

}
