
package com.bernardomg.exception;

public final class InvalidIdException extends RuntimeException {

    private static final long serialVersionUID = -1589290420417721821L;

    private final Object      id;

    public InvalidIdException(final String entity, final Object id) {
        super(String.format("No entity of type %s with id %s", entity, id));

        this.id = id;
    }

    public Object getId() {
        return id;
    }

}
