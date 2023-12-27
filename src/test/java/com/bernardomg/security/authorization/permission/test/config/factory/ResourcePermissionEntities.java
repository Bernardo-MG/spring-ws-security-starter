
package com.bernardomg.security.authorization.permission.test.config.factory;

import com.bernardomg.security.authorization.permission.persistence.model.ResourcePermissionEntity;

public final class ResourcePermissionEntities {

    public static final ResourcePermissionEntity valid() {
        return ResourcePermissionEntity.builder()
            .withResource("resource")
            .withAction("action")
            .build();
    }

    private ResourcePermissionEntities() {
        super();
    }

}
