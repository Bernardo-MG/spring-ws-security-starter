
package com.bernardomg.security.user.test.role.util.model;

import com.bernardomg.security.user.model.request.RoleUpdate;
import com.bernardomg.security.user.model.request.ValidatedRoleUpdate;

public final class RolesUpdate {

    public static final RoleUpdate valid() {
        return ValidatedRoleUpdate.builder()
            .id(1L)
            .name("Role")
            .build();
    }

}
