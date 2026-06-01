
package com.bernardomg.security.permission.test.config.factory;

import com.bernardomg.security.permission.domain.model.ResourcePermission;

public final class ResourcePermissions {

    public static final ResourcePermission create() {
        return new ResourcePermission(PermissionConstants.DATA, PermissionConstants.CREATE);
    }

    public static final ResourcePermission createAlternative() {
        return new ResourcePermission(PermissionConstants.DATA_2, PermissionConstants.CREATE);
    }

    public static final ResourcePermission delete() {
        return new ResourcePermission(PermissionConstants.DATA, PermissionConstants.DELETE);
    }

    public static final ResourcePermission deleteAlternative() {
        return new ResourcePermission(PermissionConstants.DATA_2, PermissionConstants.DELETE);
    }

    public static final ResourcePermission read() {
        return new ResourcePermission(PermissionConstants.DATA, PermissionConstants.READ);
    }

    public static final ResourcePermission readAlternative() {
        return new ResourcePermission(PermissionConstants.DATA_2, PermissionConstants.READ);
    }

    public static final ResourcePermission update() {
        return new ResourcePermission(PermissionConstants.DATA, PermissionConstants.UPDATE);
    }

    public static final ResourcePermission updateAlternative() {
        return new ResourcePermission(PermissionConstants.DATA_2, PermissionConstants.UPDATE);
    }

    private ResourcePermissions() {
        super();
    }

}
