
package com.bernardomg.security.authorization.permission.test.config.factory;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ResourcePermissionEntity;

public final class ResourcePermissionEntities {

    public static final ResourcePermissionEntity create() {
        return ResourcePermissionEntity.builder()
            .withName(PermissionConstants.DATA + ":" + PermissionConstants.CREATE)
            .withResource(PermissionConstants.DATA)
            .withAction(PermissionConstants.CREATE)
            .build();
    }

    @Deprecated
    public static final ResourcePermissionEntity valid() {
        return ResourcePermissionEntity.builder()
            .withResource(PermissionConstants.RESOURCE)
            .withAction(PermissionConstants.ACTION)
            .build();
    }

    private ResourcePermissionEntities() {
        super();
    }

}
