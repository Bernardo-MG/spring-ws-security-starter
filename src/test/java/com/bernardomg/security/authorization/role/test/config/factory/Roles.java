
package com.bernardomg.security.authorization.role.test.config.factory;

import com.bernardomg.security.authorization.role.domain.model.Role;

public final class Roles {

    public static final Role alternative() {
        return Role.builder()
            .withName(RoleConstants.ALTERNATIVE_NAME)
            .build();
    }

    public static final Role valid() {
        return Role.builder()
            .withName(RoleConstants.NAME)
            .build();
    }

    private Roles() {
        super();
    }

}
