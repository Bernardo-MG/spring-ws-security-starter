
package com.bernardomg.security.user.test.config.factory;

import java.util.Collection;
import java.util.List;

import com.bernardomg.security.domain.permission.model.ResourcePermission;
import com.bernardomg.security.domain.role.model.Role;

public final class Roles {

    public static final Role withSinglePermission() {
        final Collection<ResourcePermission> permissions;

        permissions = List.of(ResourcePermissions.create());
        return new Role(RoleConstants.NAME, permissions);
    }

    private Roles() {
        super();
    }

}
