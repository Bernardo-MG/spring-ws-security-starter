
package com.bernardomg.security.authorization.permission.test.util.model;

import com.bernardomg.security.authorization.permission.persistence.model.RolePermissionEntity;

public final class RolePermissionEntities {

    public static final RolePermissionEntity granted() {
        return RolePermissionEntity.builder()
            .withPermission("DATA:CREATE")
            .withRoleId(1L)
            .withGranted(true)
            .build();
    }

    private RolePermissionEntities() {
        super();
    }

}
