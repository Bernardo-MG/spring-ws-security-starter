
package com.bernardomg.security.permission.test.config.factory;

import com.bernardomg.security.permission.domain.model.ResourcePermission;

public final class ResourcePermissions {

    public static final ResourcePermission create() {
        return ResourcePermission.builder()
            .withResource(PermissionConstants.DATA)
            .withAction(PermissionConstants.CREATE)
            .build();
    }

    public static final ResourcePermission delete() {
        return ResourcePermission.builder()
            .withResource(PermissionConstants.DATA)
            .withAction(PermissionConstants.DELETE)
            .build();
    }

    public static final ResourcePermission read() {
        return ResourcePermission.builder()
            .withResource(PermissionConstants.DATA)
            .withAction(PermissionConstants.READ)
            .build();
    }

    public static final ResourcePermission update() {
        return ResourcePermission.builder()
            .withResource(PermissionConstants.DATA)
            .withAction(PermissionConstants.UPDATE)
            .build();
    }

    private ResourcePermissions() {
        super();
    }

}
