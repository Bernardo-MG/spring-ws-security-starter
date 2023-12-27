
package com.bernardomg.security.authorization.role.test.config.factory;

import com.bernardomg.security.authorization.role.model.request.RoleChange;

public final class RolesUpdate {

    public static final RoleChange valid() {
        return RoleChange.builder()
            .withName(Roles.NAME)
            .build();
    }

}
