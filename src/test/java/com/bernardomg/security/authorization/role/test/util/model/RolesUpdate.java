
package com.bernardomg.security.authorization.role.test.util.model;

import com.bernardomg.security.authentication.user.model.query.RoleUpdateRequest;
import com.bernardomg.security.authorization.role.model.request.RoleUpdate;

public final class RolesUpdate {

    public static final RoleUpdate valid() {
        return RoleUpdateRequest.builder()
            .id(1L)
            .name("Role")
            .build();
    }

}
