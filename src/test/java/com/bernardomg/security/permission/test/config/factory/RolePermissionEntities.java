
package com.bernardomg.security.permission.test.config.factory;

import com.bernardomg.security.role.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.role.adapter.inbound.jpa.model.RolePermissionId;

public final class RolePermissionEntities {

    public static final RolePermissionEntity create() {
        final RolePermissionId id;

        id = RolePermissionId.builder()
            .withPermission(PermissionConstants.DATA_CREATE)
            .withRoleId(1L)
            .build();
        return RolePermissionEntity.builder()
            .withId(id)
            .withResourcePermission(ResourcePermissionEntities.create())
            .withGranted(true)
            .build();
    }

    public static final RolePermissionEntity delete() {
        final RolePermissionId id;

        id = RolePermissionId.builder()
            .withPermission(PermissionConstants.DATA_DELETE)
            .withRoleId(1L)
            .build();
        return RolePermissionEntity.builder()
            .withId(id)
            .withResourcePermission(ResourcePermissionEntities.delete())
            .withGranted(true)
            .build();
    }

    public static final RolePermissionEntity granted() {
        final RolePermissionId id;

        id = RolePermissionId.builder()
            .withPermission(PermissionConstants.DATA_CREATE)
            .withRoleId(1L)
            .build();
        return RolePermissionEntity.builder()
            .withId(id)
            .withResourcePermission(ResourcePermissionEntities.create())
            .withGranted(true)
            .build();
    }

    public static final RolePermissionEntity notGranted() {
        final RolePermissionId id;

        id = RolePermissionId.builder()
            .withPermission(PermissionConstants.DATA_CREATE)
            .withRoleId(1L)
            .build();
        return RolePermissionEntity.builder()
            .withId(id)
            .withResourcePermission(ResourcePermissionEntities.create())
            .withGranted(false)
            .build();
    }

    public static final RolePermissionEntity read() {
        final RolePermissionId id;

        id = RolePermissionId.builder()
            .withPermission(PermissionConstants.DATA_READ)
            .withRoleId(1L)
            .build();
        return RolePermissionEntity.builder()
            .withId(id)
            .withResourcePermission(ResourcePermissionEntities.read())
            .withGranted(true)
            .build();
    }

    public static final RolePermissionEntity update() {
        final RolePermissionId id;

        id = RolePermissionId.builder()
            .withPermission(PermissionConstants.DATA_UPDATE)
            .withRoleId(1L)
            .build();
        return RolePermissionEntity.builder()
            .withId(id)
            .withResourcePermission(ResourcePermissionEntities.update())
            .withGranted(true)
            .build();
    }

    private RolePermissionEntities() {
        super();
    }

}
