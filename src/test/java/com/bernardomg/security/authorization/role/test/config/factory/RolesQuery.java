
package com.bernardomg.security.authorization.role.test.config.factory;

import com.bernardomg.security.authorization.role.domain.model.request.RoleQuery;

public final class RolesQuery {

    public static final RoleQuery empty() {
        return RoleQuery.builder()
            .build();
    }

}
