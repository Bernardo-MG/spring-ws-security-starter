
package com.bernardomg.security.authorization.role.test.config.factory;

import com.bernardomg.security.authorization.role.domain.model.request.RoleChange;

public final class RolesUpdate {

    public static final RoleChange valid() {
        return RoleChange.builder()
            .withName(RoleConstants.NAME)
            .build();
    }

}
