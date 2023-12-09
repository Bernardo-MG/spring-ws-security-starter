
package com.bernardomg.security.permission.test.util.model;

import com.bernardomg.security.authorization.permission.persistence.model.RolePermissionEntity;

public final class RolePermissionEntities {

    public static final RolePermissionEntity granted() {
        return RolePermissionEntity.builder()
            .withPermissionId(1L)
            .withRoleId(1L)
            .withGranted(true)
            .build();
    }

    private RolePermissionEntities() {
        super();
    }

}
