
package com.bernardomg.security.role.test.config.factory;

import java.util.List;

import com.bernardomg.security.permission.test.config.factory.ResourcePermissionEntities;
import com.bernardomg.security.role.adapter.inbound.jpa.model.RoleEntity;

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
        final RoleEntity entity;

        entity = new RoleEntity();
        entity.setId(1L);
        entity.setName(RoleConstants.NAME);
        entity.setPermissions(List.of(ResourcePermissionEntities.create()));

        return entity;
    }

    public static final RoleEntity withPermissions() {
        final RoleEntity entity;

        entity = new RoleEntity();
        entity.setId(1L);
        entity.setName(RoleConstants.NAME);
        entity.setPermissions(List.of(ResourcePermissionEntities.create(), ResourcePermissionEntities.delete(),
            ResourcePermissionEntities.read(), ResourcePermissionEntities.update()));

        return entity;
    }

    private RoleEntities() {
        super();
    }

}
