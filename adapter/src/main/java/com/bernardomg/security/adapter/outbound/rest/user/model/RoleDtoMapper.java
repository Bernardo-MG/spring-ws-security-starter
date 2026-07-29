
package com.bernardomg.security.adapter.outbound.rest.user.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.bernardomg.security.adapter.outbound.rest.user.dto.AuditDetailsDto;
import com.bernardomg.security.adapter.outbound.rest.user.dto.AuditUserDto;
import com.bernardomg.security.adapter.outbound.rest.user.dto.ResourcePermissionDto;
import com.bernardomg.security.adapter.outbound.rest.user.dto.RoleChangeDto;
import com.bernardomg.security.adapter.outbound.rest.user.dto.RoleCreationDto;
import com.bernardomg.security.adapter.outbound.rest.user.dto.RoleDto;
import com.bernardomg.security.domain.audit.model.AuditDetails;
import com.bernardomg.security.domain.audit.model.AuditDetails.AuditUser;
import com.bernardomg.security.domain.permission.model.ResourcePermission;
import com.bernardomg.security.domain.role.model.Role;

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
            .permissions(permissions)
            .audit(toDto(role.audit()));
    }

    private static final AuditDetailsDto toDto(final AuditDetails audit) {
        final AuditDetailsDto dto;

        if (audit == null) {
            dto = null;
        } else {
            dto = new AuditDetailsDto().createdAt(audit.createdAt())
                .createdBy(toDto(audit.createdBy()))
                .updatedAt(audit.updatedAt())
                .updatedBy(toDto(audit.updatedBy()));
        }

        return dto;
    }

    private static final AuditUserDto toDto(final AuditUser user) {
        final AuditUserDto dto;

        if (user == null) {
            dto = null;
        } else {
            dto = new AuditUserDto().email(user.email())
                .username(user.username())
                .name(user.name());
        }
        return dto;
    }

    private static final ResourcePermissionDto toDto(final ResourcePermission permission) {
        return new ResourcePermissionDto().resource(permission.resource())
            .action(permission.action());
    }

    private RoleDtoMapper() {
        super();
    }

}
