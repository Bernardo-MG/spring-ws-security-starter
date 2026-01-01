
package com.bernardomg.security.permission.test.config.factory;

import com.bernardomg.security.role.adapter.inbound.jpa.model.RolePermissionEntity;

public final class RolePermissionEntities {

    public static final RolePermissionEntity create() {
        final RolePermissionEntity entity;

        entity = new RolePermissionEntity();
        entity.setRoleId(1L);
        entity.setPermissionId(1L);
        entity.setGranted(true);
        entity.setResourcePermission(ResourcePermissionEntities.create());

        return entity;
    }

    public static final RolePermissionEntity delete() {
        final RolePermissionEntity entity;

        entity = new RolePermissionEntity();
        entity.setRoleId(1L);
        entity.setPermissionId(1L);
        entity.setGranted(true);
        entity.setResourcePermission(ResourcePermissionEntities.delete());

        return entity;
    }

    public static final RolePermissionEntity granted() {
        final RolePermissionEntity entity;

        entity = new RolePermissionEntity();
        entity.setRoleId(1L);
        entity.setPermissionId(1L);
        entity.setGranted(true);
        entity.setResourcePermission(ResourcePermissionEntities.create());

        return entity;
    }

    public static final RolePermissionEntity notGranted() {
        final RolePermissionEntity entity;

        entity = new RolePermissionEntity();
        entity.setRoleId(1L);
        entity.setPermissionId(1L);
        entity.setGranted(false);
        entity.setResourcePermission(ResourcePermissionEntities.create());

        return entity;
    }

    public static final RolePermissionEntity read() {
        final RolePermissionEntity entity;

        entity = new RolePermissionEntity();
        entity.setRoleId(1L);
        entity.setPermissionId(1L);
        entity.setGranted(true);
        entity.setResourcePermission(ResourcePermissionEntities.read());

        return entity;
    }

    public static final RolePermissionEntity update() {
        final RolePermissionEntity entity;

        entity = new RolePermissionEntity();
        entity.setRoleId(1L);
        entity.setPermissionId(1L);
        entity.setGranted(true);
        entity.setResourcePermission(ResourcePermissionEntities.update());

        return entity;
    }

    private RolePermissionEntities() {
        super();
    }

}
