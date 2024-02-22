
package com.bernardomg.security.authorization.permission.test.config.factory;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.RolePermissionEntity;

public final class RolePermissionEntities {

    public static final RolePermissionEntity create() {
        return RolePermissionEntity.builder()
            .withRoleId(1L)
            .withPermission(PermissionConstants.DATA_CREATE)
            .withResourcePermission(ResourcePermissionEntities.create())
            .withGranted(true)
            .build();
    }

    public static final RolePermissionEntity delete() {
        return RolePermissionEntity.builder()
            .withRoleId(1L)
            .withPermission(PermissionConstants.DATA_DELETE)
            .withResourcePermission(ResourcePermissionEntities.delete())
            .withGranted(true)
            .build();
    }

    public static final RolePermissionEntity granted() {
        return RolePermissionEntity.builder()
            .withRoleId(1L)
            .withPermission(PermissionConstants.DATA_CREATE)
            .withResourcePermission(ResourcePermissionEntities.create())
            .withGranted(true)
            .build();
    }

    public static final RolePermissionEntity notGranted() {
        return RolePermissionEntity.builder()
            .withRoleId(1L)
            .withPermission(PermissionConstants.DATA_CREATE)
            .withResourcePermission(ResourcePermissionEntities.create())
            .withGranted(false)
            .build();
    }

    public static final RolePermissionEntity read() {
        return RolePermissionEntity.builder()
            .withRoleId(1L)
            .withPermission(PermissionConstants.DATA_READ)
            .withResourcePermission(ResourcePermissionEntities.read())
            .withGranted(true)
            .build();
    }

    public static final RolePermissionEntity update() {
        return RolePermissionEntity.builder()
            .withRoleId(1L)
            .withPermission(PermissionConstants.DATA_UPDATE)
            .withResourcePermission(ResourcePermissionEntities.update())
            .withGranted(true)
            .build();
    }

    private RolePermissionEntities() {
        super();
    }

}
