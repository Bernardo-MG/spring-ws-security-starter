
package com.bernardomg.security.authorization.role.test.util.model;

import com.bernardomg.security.authorization.role.persistence.model.UserRoleEntity;

public final class UserRoleEntities {

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
