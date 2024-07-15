
package com.bernardomg.security.authorization.role.test.config.factory;

import java.util.Collection;
import java.util.List;

import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.permission.test.config.factory.RolePermissionEntities;

public final class RoleEntities {

    public static final RoleEntity alternative() {
        return RoleEntity.builder()
            .withId(2L)
            .withName(RoleConstants.ALTERNATIVE_NAME)
            .withPermissions(List.of())
            .build();
    }

    public static final RoleEntity withoutPermissions() {
        return RoleEntity.builder()
            .withId(1L)
            .withName(RoleConstants.NAME)
            .withPermissions(List.of())
            .build();
    }

    public static final RoleEntity withPermission() {
        final Collection<RolePermissionEntity> permissions;
        final RolePermissionEntity             create;

        create = RolePermissionEntities.create();

        create.getResourcePermission()
            .setId(1L);

        permissions = List.of(create);
        return RoleEntity.builder()
            .withId(1L)
            .withName(RoleConstants.NAME)
            .withPermissions(permissions)
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

        permissions = List.of(create, delete, read, update);
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
