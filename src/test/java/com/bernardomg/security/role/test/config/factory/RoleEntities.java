
package com.bernardomg.security.role.test.config.factory;

import java.util.List;

import com.bernardomg.security.permission.test.config.factory.RolePermissionEntities;
import com.bernardomg.security.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.role.adapter.inbound.jpa.model.RolePermissionEntity;

public final class RoleEntities {

    public static final RoleEntity alternative() {
        final RoleEntity entity;

        entity = new RoleEntity();
        entity.setId(2L);
        entity.setName(RoleConstants.ALTERNATIVE_NAME);
        entity.setPermissions(List.of());

        return entity;
    }

    public static final RoleEntity withoutPermissions() {
        final RoleEntity entity;

        entity = new RoleEntity();
        entity.setId(1L);
        entity.setName(RoleConstants.NAME);
        entity.setPermissions(List.of());

        return entity;
    }

    public static final RoleEntity withPermission() {
        final RolePermissionEntity create;
        final RoleEntity           entity;

        create = RolePermissionEntities.create();

        create.getResourcePermission()
            .setId(1L);

        entity = new RoleEntity();
        entity.setId(1L);
        entity.setName(RoleConstants.NAME);
        entity.setPermissions(List.of(create));

        return entity;
    }

    public static final RoleEntity withPermissions() {
        final RolePermissionEntity create;
        final RolePermissionEntity read;
        final RolePermissionEntity update;
        final RolePermissionEntity delete;
        final RoleEntity           entity;

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

        entity = new RoleEntity();
        entity.setId(1L);
        entity.setName(RoleConstants.NAME);
        entity.setPermissions(List.of(create, delete, read, update));

        return entity;
    }

    private RoleEntities() {
        super();
    }

}
