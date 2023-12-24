
package com.bernardomg.security.authorization.role.test.config.factory;

import com.bernardomg.security.authorization.role.persistence.model.RoleEntity;

public final class RoleEntities {

    public static final RoleEntity valid() {
        return RoleEntity.builder()
            .withId(1L)
            .withName(Roles.NAME)
            .build();
    }

    private RoleEntities() {
        super();
    }

}
