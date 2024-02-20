
package com.bernardomg.security.authorization.permission.test.config.factory;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.RolePermissionEntity;

public final class RolePermissionEntities {

    public static final RolePermissionEntity create() {
        return RolePermissionEntity.builder()
            .withResourcePermission(ResourcePermissionEntities.create())
            .withGranted(true)
            .build();
    }

    public static final RolePermissionEntity delete() {
        return RolePermissionEntity.builder()
            .withResourcePermission(ResourcePermissionEntities.delete())
            .withGranted(true)
            .build();
    }

    public static final RolePermissionEntity granted() {
        return RolePermissionEntity.builder()
            .withResourcePermission(ResourcePermissionEntities.create())
            .withGranted(true)
            .build();
    }

    public static final RolePermissionEntity notGranted() {
        return RolePermissionEntity.builder()
            .withResourcePermission(ResourcePermissionEntities.create())
            .withGranted(false)
            .build();
    }

    public static final RolePermissionEntity read() {
        return RolePermissionEntity.builder()
            .withResourcePermission(ResourcePermissionEntities.read())
            .withGranted(true)
            .build();
    }

    public static final RolePermissionEntity update() {
        return RolePermissionEntity.builder()
            .withResourcePermission(ResourcePermissionEntities.update())
            .withGranted(true)
            .build();
    }

    private RolePermissionEntities() {
        super();
    }

}
