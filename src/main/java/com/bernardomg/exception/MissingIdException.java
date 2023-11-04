
package com.bernardomg.exception;

import java.io.Serializable;

public class MissingIdException extends RuntimeException {

    private static final long  serialVersionUID = -1589290420417721821L;

    private final Serializable id;

    public MissingIdException(final String resource, final Serializable id) {
        super(String.format("Missing id %s for %s", id, resource));

        this.id = id;
    }

    public Object getId() {
        return id;
    }

}
