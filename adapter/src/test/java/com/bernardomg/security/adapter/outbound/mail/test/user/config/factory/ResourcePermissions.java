
package com.bernardomg.security.adapter.outbound.mail.test.user.config.factory;

import com.bernardomg.security.domain.permission.model.ResourcePermission;

public final class ResourcePermissions {

    public static final ResourcePermission create() {
        return new ResourcePermission(PermissionConstants.DATA, PermissionConstants.CREATE);
    }

    private ResourcePermissions() {
        super();
    }

}
