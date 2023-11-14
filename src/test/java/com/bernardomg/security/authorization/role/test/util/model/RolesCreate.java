
package com.bernardomg.security.authorization.role.test.util.model;

import com.bernardomg.security.authorization.role.model.request.RoleCreate;
import com.bernardomg.security.authorization.role.model.request.RoleCreateRequest;

public final class RolesCreate {

    public static final RoleCreate name(final String name) {
        return RoleCreateRequest.builder()
            .name(name)
            .build();
    }

    public static final RoleCreate valid() {
        return RoleCreateRequest.builder()
            .name("Role")
            .build();
    }

}
