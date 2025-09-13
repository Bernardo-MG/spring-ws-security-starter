
package com.bernardomg.security.role.adapter.outbound.rest.model;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Sorting.Direction;
import com.bernardomg.data.domain.Sorting.Property;
import com.bernardomg.security.permission.data.domain.model.ResourcePermission;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.ucronia.openapi.model.PropertyDto;
import com.bernardomg.ucronia.openapi.model.PropertyDto.DirectionEnum;
import com.bernardomg.ucronia.openapi.model.ResourcePermissionDto;
import com.bernardomg.ucronia.openapi.model.RoleChangeDto;
import com.bernardomg.ucronia.openapi.model.RoleDto;
import com.bernardomg.ucronia.openapi.model.RolePageResponseDto;
import com.bernardomg.ucronia.openapi.model.RoleResponseDto;
import com.bernardomg.ucronia.openapi.model.SortingDto;

public final class RoleDtoMapper {

    public static final Role toDomain(final RoleChangeDto roleChangeDto, final String roleName) {
        final Collection<ResourcePermission> permissions;

        if (roleChangeDto.getPermissions() == null) {
            permissions = List.of();
        } else {
            permissions = roleChangeDto.getPermissions()
                .stream()
                .map(p -> new ResourcePermission(p.getResource(), p.getAction()))
                .toList();
        }
        return new Role(roleName, permissions);
    }

    public static final RoleResponseDto toResponseDto(final Optional<Role> role) {
        return new RoleResponseDto().content(role.map(RoleDtoMapper::toDto)
            .orElse(null));
    }

    public static final RolePageResponseDto toResponseDto(final Page<Role> page) {
        final SortingDto sortingResponse;

        sortingResponse = new SortingDto().properties(page.sort()
            .properties()
            .stream()
            .map(RoleDtoMapper::toDto)
            .toList());
        return new RolePageResponseDto().content(page.content()
            .stream()
            .map(RoleDtoMapper::toDto)
            .toList())
            .size(page.size())
            .page(page.page())
            .totalElements(page.totalElements())
            .totalPages(page.totalPages())
            .elementsInPage(page.elementsInPage())
            .first(page.first())
            .last(page.last())
            .sort(sortingResponse);
    }

    public static final RoleResponseDto toResponseDto(final Role role) {
        return new RoleResponseDto().content(toDto(role));
    }

    private static final PropertyDto toDto(final Property property) {
        final DirectionEnum direction;

        if (property.direction() == Direction.ASC) {
            direction = DirectionEnum.ASC;
        } else {
            direction = DirectionEnum.DESC;
        }
        return new PropertyDto().name(property.name())
            .direction(direction);
    }

    private static final ResourcePermissionDto toDto(final ResourcePermission permission) {
        return new ResourcePermissionDto().resource(permission.resource())
            .action(permission.action());
    }

    private static final RoleDto toDto(final Role role) {
        final List<ResourcePermissionDto> permissions;

        permissions = role.permissions()
            .stream()
            .map(RoleDtoMapper::toDto)
            .toList();
        return new RoleDto().name(role.name())
            .permissions(permissions);
    }

    private RoleDtoMapper() {
        super();
    }

}
