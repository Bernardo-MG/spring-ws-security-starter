
package com.bernardomg.security.user.test.role.util.model;

import com.bernardomg.security.authentication.user.model.query.ValidatedRoleUpdate;
import com.bernardomg.security.authorization.role.model.request.RoleUpdate;

public final class RolesUpdate {

    public static final RoleUpdate valid() {
        return ValidatedRoleUpdate.builder()
            .id(1L)
            .name("Role")
            .build();
    }

}
