
package com.bernardomg.security.authorization.permission.test.util.model;

import com.bernardomg.security.authorization.permission.persistence.model.RolePermissionEntity;

public final class RolePermissionEntities {

    public static final RolePermissionEntity create() {
        return RolePermissionEntity.builder()
            .withPermission("DATA:CREATE")
            .withRoleId(1L)
            .withGranted(true)
            .build();
    }

    public static final RolePermissionEntity createNotGranted() {
        return RolePermissionEntity.builder()
            .withPermission("DATA:CREATE")
            .withRoleId(1L)
            .withGranted(false)
            .build();
    }

    public static final RolePermissionEntity delete() {
        return RolePermissionEntity.builder()
            .withPermission("DATA:DELETE")
            .withRoleId(1L)
            .withGranted(true)
            .build();
    }

    public static final RolePermissionEntity granted() {
        return RolePermissionEntity.builder()
            .withPermission("DATA:CREATE")
            .withRoleId(1L)
            .withGranted(true)
            .build();
    }

    public static final RolePermissionEntity read() {
        return RolePermissionEntity.builder()
            .withPermission("DATA:READ")
            .withRoleId(1L)
            .withGranted(true)
            .build();
    }

    public static final RolePermissionEntity update() {
        return RolePermissionEntity.builder()
            .withPermission("DATA:UPDATE")
            .withRoleId(1L)
            .withGranted(true)
            .build();
    }

    private RolePermissionEntities() {
        super();
    }

}
