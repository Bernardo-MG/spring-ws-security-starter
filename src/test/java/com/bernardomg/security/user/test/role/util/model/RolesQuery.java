
package com.bernardomg.security.user.test.role.util.model;

import com.bernardomg.security.user.model.request.RoleQuery;
import com.bernardomg.security.user.model.request.ValidatedRoleQuery;

public final class RolesQuery {

    public static final RoleQuery empty() {
        return ValidatedRoleQuery.builder()
            .build();
    }

}
