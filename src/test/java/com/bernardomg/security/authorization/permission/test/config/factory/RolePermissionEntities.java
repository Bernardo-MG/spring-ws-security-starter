
package com.bernardomg.security.authorization.permission.test.config.factory;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.RolePermissionEntity;

public final class RolePermissionEntities {

    public static final RolePermissionEntity create() {
        return RolePermissionEntity.builder()
            .withPermission(PermissionConstants.DATA_CREATE)
            .withRoleId(1L)
            .withGranted(true)
            .build();
    }

    public static final RolePermissionEntity delete() {
        return RolePermissionEntity.builder()
            .withPermission(PermissionConstants.DATA_DELETE)
            .withRoleId(1L)
            .withGranted(true)
            .build();
    }

    public static final RolePermissionEntity granted() {
        return RolePermissionEntity.builder()
            .withPermission(PermissionConstants.DATA_CREATE)
            .withRoleId(1L)
            .withGranted(true)
            .build();
    }

    public static final RolePermissionEntity notGranted() {
        return RolePermissionEntity.builder()
            .withPermission(PermissionConstants.DATA_CREATE)
            .withRoleId(1L)
            .withGranted(false)
            .build();
    }

    public static final RolePermissionEntity read() {
        return RolePermissionEntity.builder()
            .withPermission(PermissionConstants.DATA_READ)
            .withRoleId(1L)
            .withGranted(true)
            .build();
    }

    public static final RolePermissionEntity update() {
        return RolePermissionEntity.builder()
            .withPermission(PermissionConstants.DATA_UPDATE)
            .withRoleId(1L)
            .withGranted(true)
            .build();
    }

    private RolePermissionEntities() {
        super();
    }

}
