
package com.bernardomg.security.user.test.role.util.model;

import com.bernardomg.security.user.model.request.RoleCreate;
import com.bernardomg.security.user.model.request.ValidatedRoleCreate;

public final class RolesCreate {

    public static final RoleCreate name(final String name) {
        return ValidatedRoleCreate.builder()
            .name(name)
            .build();
    }

    public static final RoleCreate valid() {
        return ValidatedRoleCreate.builder()
            .name("Role")
            .build();
    }

}
