
package com.bernardomg.security.adapter.outbound.rest.user.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.bernardomg.security.domain.permission.model.ResourcePermission;
import com.bernardomg.security.domain.role.model.Role;
import com.bernardomg.security.user.adapter.outbound.rest.dto.ResourcePermissionDto;
import com.bernardomg.security.user.adapter.outbound.rest.dto.RoleChangeDto;
import com.bernardomg.security.user.adapter.outbound.rest.dto.RoleCreationDto;
import com.bernardomg.security.user.adapter.outbound.rest.dto.RoleDto;

public final class RoleDtoMapper {

    public static final Role toDomain(final RoleChangeDto roleChangeDto, final String roleName) {
        final Collection<ResourcePermission> permissions;

        if (roleChangeDto.getPermissions() == null) {
            permissions = List.of();
        } else {
            permissions = roleChangeDto.getPermissions()
                .stream()
                .map(p -> new ResourcePermission(p.getResource(), p.getAction()))
                .collect(Collectors.toCollection(ArrayList::new));
        }
        return new Role(roleName, permissions);
    }

    public static final Role toDomain(final RoleCreationDto roleCreationDto) {
        final Collection<ResourcePermission> permissions;

        if (roleCreationDto.getPermissions() == null) {
            permissions = List.of();
        } else {
            permissions = roleCreationDto.getPermissions()
                .stream()
                .map(p -> new ResourcePermission(p.getResource(), p.getAction()))
                .collect(Collectors.toCollection(ArrayList::new));
        }
        return new Role(roleCreationDto.getName(), permissions);
    }

    public static final RoleDto toDto(final Role role) {
        final List<ResourcePermissionDto> permissions;

        permissions = role.permissions()
            .stream()
            .map(RoleDtoMapper::toDto)
            .collect(Collectors.toCollection(ArrayList::new));
        return new RoleDto().name(role.name())
            .permissions(permissions);
    }

    private static final ResourcePermissionDto toDto(final ResourcePermission permission) {
        return new ResourcePermissionDto().resource(permission.resource())
            .action(permission.action());
    }

    private RoleDtoMapper() {
        super();
    }

}
