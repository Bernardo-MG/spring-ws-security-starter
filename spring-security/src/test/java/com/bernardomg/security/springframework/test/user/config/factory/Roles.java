
package com.bernardomg.security.springframework.test.user.config.factory;

import java.util.Collection;
import java.util.List;

import com.bernardomg.security.domain.permission.model.ResourcePermission;
import com.bernardomg.security.domain.role.model.Role;
import com.bernardomg.security.springframework.test.permission.config.factory.ResourcePermissions;

public final class Roles {

    public static final Role alternative() {
        return new Role(RoleConstants.ALTERNATIVE_NAME, List.of());
    }

    public static final Role duplicatedPermission() {
        final Collection<ResourcePermission> permissions;

        permissions = List.of(ResourcePermissions.create(), ResourcePermissions.create());
        return new Role(RoleConstants.NAME, permissions);
    }

    public static final Role noName() {
        return new Role("", List.of());
    }

    public static final Role withoutPermissions() {
        return new Role(RoleConstants.NAME, List.of());
    }

    public static final Role withPermissions() {
        final Collection<ResourcePermission> permissions;

        permissions = List.of(ResourcePermissions.create(), ResourcePermissions.delete(), ResourcePermissions.read(),
            ResourcePermissions.update());
        return new Role(RoleConstants.NAME, permissions);
    }

    public static final Role withSinglePermission() {
        final Collection<ResourcePermission> permissions;

        permissions = List.of(ResourcePermissions.create());
        return new Role(RoleConstants.NAME, permissions);
    }

    private Roles() {
        super();
    }

}
