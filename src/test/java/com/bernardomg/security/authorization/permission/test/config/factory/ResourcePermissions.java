
package com.bernardomg.security.authorization.permission.test.config.factory;

import com.bernardomg.security.authorization.permission.model.ResourcePermission;

public final class ResourcePermissions {

    public static final ResourcePermission create() {
        return ResourcePermission.builder()
            .withName(PermissionConstants.DATA_CREATE)
            .withResource(PermissionConstants.DATA)
            .withAction(PermissionConstants.CREATE)
            .build();
    }

    public static final ResourcePermission delete() {
        return ResourcePermission.builder()
            .withName(PermissionConstants.DATA_DELETE)
            .withResource(PermissionConstants.DATA)
            .withAction(PermissionConstants.DELETE)
            .build();
    }

    public static final ResourcePermission read() {
        return ResourcePermission.builder()
            .withName(PermissionConstants.DATA_READ)
            .withResource(PermissionConstants.DATA)
            .withAction(PermissionConstants.READ)
            .build();
    }

    public static final ResourcePermission update() {
        return ResourcePermission.builder()
            .withName(PermissionConstants.DATA_UPDATE)
            .withResource(PermissionConstants.DATA)
            .withAction(PermissionConstants.UPDATE)
            .build();
    }

    private ResourcePermissions() {
        super();
    }

}
