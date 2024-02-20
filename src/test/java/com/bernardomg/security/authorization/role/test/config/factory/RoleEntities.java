
package com.bernardomg.security.authorization.role.test.config.factory;

import java.util.Collection;
import java.util.List;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.test.config.factory.RolePermissionEntities;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;

public final class RoleEntities {

    public static final RoleEntity valid() {
        return RoleEntity.builder()
            .withId(1L)
            .withName(RoleConstants.NAME)
            .withPermissions(List.of())
            .build();
    }

    public static final RoleEntity withPermissions() {
        final Collection<RolePermissionEntity> permissions;
        final RolePermissionEntity             create;
        final RolePermissionEntity             read;
        final RolePermissionEntity             update;
        final RolePermissionEntity             delete;

        create = RolePermissionEntities.create();
        read = RolePermissionEntities.read();
        update = RolePermissionEntities.update();
        delete = RolePermissionEntities.delete();

        create.getResourcePermission()
            .setId(1L);
        read.getResourcePermission()
            .setId(2L);
        update.getResourcePermission()
            .setId(3L);
        delete.getResourcePermission()
            .setId(4L);

        permissions = List.of(create, read, update, delete);
        return RoleEntity.builder()
            .withId(1L)
            .withName(RoleConstants.NAME)
            .withPermissions(permissions)
            .build();
    }

    private RoleEntities() {
        super();
    }

}
