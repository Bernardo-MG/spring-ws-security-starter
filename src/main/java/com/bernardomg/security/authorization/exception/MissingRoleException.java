
package com.bernardomg.security.authorization.exception;

import java.io.Serializable;

import com.bernardomg.exception.MissingIdException;

public final class MissingRoleException extends MissingIdException {

    private static final long serialVersionUID = 2786821546505029631L;

    public MissingRoleException(final Serializable id) {
        super("role", id);
    }

}
