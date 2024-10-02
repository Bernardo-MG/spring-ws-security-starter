
package com.bernardomg.security.permission.test.config.factory;

import com.bernardomg.security.permission.data.domain.model.ResourcePermission;

public final class ResourcePermissions {

    public static final ResourcePermission create() {
        return new ResourcePermission(PermissionConstants.DATA, PermissionConstants.CREATE);
    }

    public static final ResourcePermission delete() {
        return new ResourcePermission(PermissionConstants.DATA, PermissionConstants.DELETE);
    }

    public static final ResourcePermission read() {
        return new ResourcePermission(PermissionConstants.DATA, PermissionConstants.READ);
    }

    public static final ResourcePermission update() {
        return new ResourcePermission(PermissionConstants.DATA, PermissionConstants.UPDATE);
    }

    private ResourcePermissions() {
        super();
    }

}
