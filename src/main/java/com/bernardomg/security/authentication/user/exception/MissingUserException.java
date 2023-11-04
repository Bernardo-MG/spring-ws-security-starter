
package com.bernardomg.security.authentication.user.exception;

import java.io.Serializable;

import com.bernardomg.exception.MissingIdException;

public final class MissingUserException extends MissingIdException {

    private static final long serialVersionUID = 2786821546505029631L;

    public MissingUserException(final Serializable id) {
        super("user", id);
    }

}
