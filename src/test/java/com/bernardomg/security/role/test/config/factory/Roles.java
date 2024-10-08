
package com.bernardomg.security.role.test.config.factory;

import java.util.Collection;
import java.util.List;

import com.bernardomg.security.permission.data.domain.model.ResourcePermission;
import com.bernardomg.security.permission.test.config.factory.ResourcePermissions;
import com.bernardomg.security.role.domain.model.Role;

public final class Roles {

    public static final Role alternative() {
        return Role.builder()
            .withName(RoleConstants.ALTERNATIVE_NAME)
            .withPermissions(List.of())
            .build();
    }

    public static final Role duplicatedPermission() {
        final Collection<ResourcePermission> permissions;

        permissions = List.of(ResourcePermissions.create(), ResourcePermissions.create());
        return Role.builder()
            .withName(RoleConstants.NAME)
            .withPermissions(permissions)
            .build();
    }

    public static final Role noName() {
        return Role.builder()
            .withName("")
            .withPermissions(List.of())
            .build();
    }

    public static final Role withoutPermissions() {
        return Role.builder()
            .withName(RoleConstants.NAME)
            .withPermissions(List.of())
            .build();
    }

    public static final Role withPermissions() {
        final Collection<ResourcePermission> permissions;

        permissions = List.of(ResourcePermissions.create(), ResourcePermissions.delete(), ResourcePermissions.read(),
            ResourcePermissions.update());
        return Role.builder()
            .withName(RoleConstants.NAME)
            .withPermissions(permissions)
            .build();
    }

    public static final Role withSinglePermission() {
        final Collection<ResourcePermission> permissions;

        permissions = List.of(ResourcePermissions.create());
        return Role.builder()
            .withName(RoleConstants.NAME)
            .withPermissions(permissions)
            .build();
    }

    private Roles() {
        super();
    }

}
