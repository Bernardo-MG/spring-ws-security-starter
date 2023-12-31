
package com.bernardomg.security.authorization.role.test.config.factory;

import com.bernardomg.security.authorization.role.model.request.RoleUpdate;
import com.bernardomg.security.authorization.role.model.request.RoleUpdateRequest;

public final class RolesUpdate {

    public static final RoleUpdate valid() {
        return RoleUpdateRequest.builder()
            .withName(Roles.NAME)
            .build();
    }

}
