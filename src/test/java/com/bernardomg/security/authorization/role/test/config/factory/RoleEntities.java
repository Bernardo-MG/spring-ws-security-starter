
package com.bernardomg.security.authorization.role.test.config.factory;

import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;

public final class RoleEntities {

    public static final RoleEntity valid() {
        return RoleEntity.builder()
            .withId(1L)
            .withName(RoleConstants.NAME)
            .build();
    }

    private RoleEntities() {
        super();
    }

}
