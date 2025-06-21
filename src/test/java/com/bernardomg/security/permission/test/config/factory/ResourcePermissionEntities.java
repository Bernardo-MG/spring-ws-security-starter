
package com.bernardomg.security.permission.test.config.factory;

import com.bernardomg.security.permission.data.adapter.inbound.jpa.model.ResourcePermissionEntity;

public final class ResourcePermissionEntities {

    public static final ResourcePermissionEntity create() {
        final ResourcePermissionEntity entity;

        entity = new ResourcePermissionEntity();
        entity.setName(PermissionConstants.DATA_CREATE);
        entity.setResource(PermissionConstants.DATA);
        entity.setAction(PermissionConstants.CREATE);

        return entity;
    }

    public static final ResourcePermissionEntity delete() {
        final ResourcePermissionEntity entity;

        entity = new ResourcePermissionEntity();
        entity.setName(PermissionConstants.DATA_DELETE);
        entity.setResource(PermissionConstants.DATA);
        entity.setAction(PermissionConstants.DELETE);

        return entity;
    }

    public static final ResourcePermissionEntity read() {
        final ResourcePermissionEntity entity;

        entity = new ResourcePermissionEntity();
        entity.setName(PermissionConstants.DATA_READ);
        entity.setResource(PermissionConstants.DATA);
        entity.setAction(PermissionConstants.READ);

        return entity;
    }

    public static final ResourcePermissionEntity update() {
        final ResourcePermissionEntity entity;

        entity = new ResourcePermissionEntity();
        entity.setName(PermissionConstants.DATA_UPDATE);
        entity.setResource(PermissionConstants.DATA);
        entity.setAction(PermissionConstants.UPDATE);

        return entity;
    }

    private ResourcePermissionEntities() {
        super();
    }

}
