
package com.bernardomg.security.role.test.config.factory;

import com.bernardomg.security.role.adapter.inbound.jpa.model.UserRoleEntity;

public final class UserRoleEntities {

    public static final UserRoleEntity alternative() {
        return UserRoleEntity.builder()
            .withUserId(1L)
            .withRoleId(2L)
            .build();
    }

    public static final UserRoleEntity valid() {
        return UserRoleEntity.builder()
            .withUserId(1L)
            .withRoleId(1L)
            .build();
    }

    private UserRoleEntities() {
        super();
    }

}
