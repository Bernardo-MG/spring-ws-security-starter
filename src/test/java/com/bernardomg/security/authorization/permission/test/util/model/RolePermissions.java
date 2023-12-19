
package com.bernardomg.security.authorization.permission.test.util.model;

import com.bernardomg.security.authorization.role.model.RolePermission;

public final class RolePermissions {

    public static final RolePermission create() {
        return RolePermission.builder()
            .withPermission("DATA:CREATE")
            .withRoleId(1L)
            .build();
    }

    private RolePermissions() {
        super();
    }

}
