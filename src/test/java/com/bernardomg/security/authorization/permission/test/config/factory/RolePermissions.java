
package com.bernardomg.security.authorization.permission.test.config.factory;

import com.bernardomg.security.authorization.permission.domain.model.RolePermission;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;

public final class RolePermissions {

    public static final RolePermission create() {
        return RolePermission.builder()
            .withRole(RoleConstants.NAME)
            .withPermission(PermissionConstants.DATA_CREATE)
            .build();
    }

    private RolePermissions() {
        super();
    }

}
