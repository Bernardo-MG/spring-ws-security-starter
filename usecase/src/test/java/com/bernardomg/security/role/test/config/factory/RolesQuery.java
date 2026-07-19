
package com.bernardomg.security.role.test.config.factory;

import java.util.Optional;

import com.bernardomg.security.domain.role.model.RoleQuery;

public final class RolesQuery {

    public static final RoleQuery byName() {
        return new RoleQuery(Optional.of(RoleConstants.NAME));
    }

    public static final RoleQuery byNameNotExisting() {
        return new RoleQuery(Optional.of(RoleConstants.ALTERNATIVE_NAME));
    }

    public static final RoleQuery empty() {
        return new RoleQuery(Optional.empty());
    }

}
