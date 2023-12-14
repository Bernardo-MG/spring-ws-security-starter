
package com.bernardomg.security.authorization.role.test.util.model;

import com.bernardomg.security.authorization.role.model.request.RoleUpdate;
import com.bernardomg.security.authorization.role.model.request.RoleUpdateRequest;

public final class RolesUpdate {

    public static final RoleUpdate valid() {
        return RoleUpdateRequest.builder()
            .withId(1L)
            .withName(Roles.NAME)
            .build();
    }

}
