
package com.bernardomg.security.role.test.config.factory;

import com.bernardomg.security.role.domain.model.RoleQuery;

public final class RolesQuery {

    public static final RoleQuery byName() {
        return new RoleQuery(RoleConstants.NAME);
    }

    public static final RoleQuery byNameNotExisting() {
        return new RoleQuery(RoleConstants.ALTERNATIVE_NAME);
    }

    public static final RoleQuery empty() {
        return new RoleQuery(null);
    }

}
