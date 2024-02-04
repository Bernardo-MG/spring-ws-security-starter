
package com.bernardomg.security.authorization.role.test.config.factory;

import com.bernardomg.security.authorization.role.domain.model.request.RoleQuery;

public final class RolesQuery {

    public static final RoleQuery byName() {
        return RoleQuery.builder()
            .withName(RoleConstants.NAME)
            .build();
    }

    public static final RoleQuery byNameNotExisting() {
        return RoleQuery.builder()
            .withName(RoleConstants.ALTERNATIVE_NAME)
            .build();
    }

    public static final RoleQuery empty() {
        return RoleQuery.builder()
            .build();
    }

}
