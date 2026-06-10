
package com.bernardomg.security.role.test.config.factory;

import com.bernardomg.security.role.adapter.inbound.jpa.model.UserRoleEntity;

public final class UserRoleEntities {

    public static final UserRoleEntity alternative() {
        final UserRoleEntity entity;

        entity = new UserRoleEntity();
        entity.setUserId(1L);
        entity.setRoleId(2L);

        return entity;
    }

    public static final UserRoleEntity valid() {
        final UserRoleEntity entity;

        entity = new UserRoleEntity();
        entity.setUserId(1L);
        entity.setRoleId(2L);

        return entity;
    }

    private UserRoleEntities() {
        super();
    }

}
