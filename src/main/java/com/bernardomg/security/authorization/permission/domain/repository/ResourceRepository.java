
package com.bernardomg.security.authorization.permission.domain.repository;

import com.bernardomg.security.authorization.permission.domain.model.Resource;

public interface ResourceRepository {

    public boolean exists(final String name);

    public Resource save(final Resource resource);

}
