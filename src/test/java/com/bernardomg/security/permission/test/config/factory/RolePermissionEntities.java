
package com.bernardomg.security.permission.test.config.factory;

import com.bernardomg.security.role.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.role.adapter.inbound.jpa.model.RolePermissionId;

public final class RolePermissionEntities {

    public static final RolePermissionEntity create() {
        final RolePermissionId     id;
        final RolePermissionEntity entity;

        id = new RolePermissionId();
        id.setRoleId(1L);
        id.setPermissionId(1L);

        entity = new RolePermissionEntity();
        entity.setId(id);
        entity.setGranted(true);
        entity.setResourcePermission(ResourcePermissionEntities.create());

        return entity;
    }

    public static final RolePermissionEntity delete() {
        final RolePermissionId     id;
        final RolePermissionEntity entity;

        id = new RolePermissionId();
        id.setRoleId(1L);
        id.setPermissionId(1L);

        entity = new RolePermissionEntity();
        entity.setId(id);
        entity.setGranted(true);
        entity.setResourcePermission(ResourcePermissionEntities.delete());

        return entity;
    }

    public static final RolePermissionEntity granted() {
        final RolePermissionId     id;
        final RolePermissionEntity entity;

        id = new RolePermissionId();
        id.setRoleId(1L);
        id.setPermissionId(1L);

        entity = new RolePermissionEntity();
        entity.setId(id);
        entity.setGranted(true);
        entity.setResourcePermission(ResourcePermissionEntities.create());

        return entity;
    }

    public static final RolePermissionEntity notGranted() {
        final RolePermissionId     id;
        final RolePermissionEntity entity;

        id = new RolePermissionId();
        id.setRoleId(1L);
        id.setPermissionId(1L);

        entity = new RolePermissionEntity();
        entity.setId(id);
        entity.setGranted(false);
        entity.setResourcePermission(ResourcePermissionEntities.create());

        return entity;
    }

    public static final RolePermissionEntity read() {
        final RolePermissionId     id;
        final RolePermissionEntity entity;

        id = new RolePermissionId();
        id.setRoleId(1L);
        id.setPermissionId(1L);

        entity = new RolePermissionEntity();
        entity.setId(id);
        entity.setGranted(true);
        entity.setResourcePermission(ResourcePermissionEntities.read());

        return entity;
    }

    public static final RolePermissionEntity update() {
        final RolePermissionId     id;
        final RolePermissionEntity entity;

        id = new RolePermissionId();
        id.setRoleId(1L);
        id.setPermissionId(1L);

        entity = new RolePermissionEntity();
        entity.setId(id);
        entity.setGranted(true);
        entity.setResourcePermission(ResourcePermissionEntities.update());

        return entity;
    }

    private RolePermissionEntities() {
        super();
    }

}
