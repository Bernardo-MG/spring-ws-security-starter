
package com.bernardomg.security.user.test.role.util.model;

import com.bernardomg.security.authorization.role.model.request.RoleQuery;
import com.bernardomg.security.authorization.role.model.request.ValidatedRoleQuery;

public final class RolesQuery {

    public static final RoleQuery empty() {
        return ValidatedRoleQuery.builder()
            .build();
    }

}
