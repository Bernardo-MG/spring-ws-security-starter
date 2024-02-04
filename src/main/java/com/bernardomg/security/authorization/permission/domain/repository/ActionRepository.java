
package com.bernardomg.security.authorization.permission.domain.repository;

import com.bernardomg.security.authorization.permission.domain.model.Action;

public interface ActionRepository {

    public boolean exists(final String name);

    public Action save(final Action action);

}
