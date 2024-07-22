
package com.bernardomg.security.role.test.config.factory;

import com.bernardomg.security.role.domain.model.RoleQuery;

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
