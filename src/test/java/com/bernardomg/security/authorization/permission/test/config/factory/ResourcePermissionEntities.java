
package com.bernardomg.security.authorization.permission.test.config.factory;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ResourcePermissionEntity;

public final class ResourcePermissionEntities {

    public static final ResourcePermissionEntity create() {
        return ResourcePermissionEntity.builder()
            .withName(PermissionConstants.DATA_CREATE)
            .withResource(PermissionConstants.DATA)
            .withAction(PermissionConstants.CREATE)
            .build();
    }

    public static final ResourcePermissionEntity delete() {
        return ResourcePermissionEntity.builder()
            .withName(PermissionConstants.DATA_DELETE)
            .withResource(PermissionConstants.DATA)
            .withAction(PermissionConstants.DELETE)
            .build();
    }

    public static final ResourcePermissionEntity read() {
        return ResourcePermissionEntity.builder()
            .withName(PermissionConstants.DATA_READ)
            .withResource(PermissionConstants.DATA)
            .withAction(PermissionConstants.READ)
            .build();
    }

    public static final ResourcePermissionEntity update() {
        return ResourcePermissionEntity.builder()
            .withName(PermissionConstants.DATA_UPDATE)
            .withResource(PermissionConstants.DATA)
            .withAction(PermissionConstants.UPDATE)
            .build();
    }

    private ResourcePermissionEntities() {
        super();
    }

}
