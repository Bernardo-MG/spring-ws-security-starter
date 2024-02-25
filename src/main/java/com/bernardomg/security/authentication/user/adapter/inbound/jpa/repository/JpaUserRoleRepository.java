
package com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.domain.repository.UserRoleRepository;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleSpringRepository;
import com.bernardomg.security.authorization.role.domain.model.Role;

public final class JpaUserRoleRepository implements UserRoleRepository {

    private final RoleSpringRepository roleSpringRepository;

    private final UserSpringRepository userSpringRepository;

    public JpaUserRoleRepository(final RoleSpringRepository roleSpringRepo, final UserSpringRepository userSpringRepo) {
        super();

        roleSpringRepository = roleSpringRepo;
        userSpringRepository = userSpringRepo;
    }

    @Override
    public final Iterable<Role> findAvailableToUser(final String username, final Pageable page) {
        final boolean        exists;
        final Iterable<Role> roles;

        exists = userSpringRepository.existsByUsername(username);
        if (exists) {
            roles = roleSpringRepository.findAvailableToUser(username, page)
                .map(this::toDomain);
        } else {
            roles = List.of();
        }

        // TODO: this doesn't need the full role model, just the names
        return roles;
    }

    private final ResourcePermission toDomain(final ResourcePermissionEntity entity) {
        return ResourcePermission.builder()
            .withName(entity.getName())
            .withResource(entity.getResource())
            .withAction(entity.getAction())
            .build();
    }

    private final Role toDomain(final RoleEntity role) {
        final Collection<ResourcePermission> permissions;

        if (role.getPermissions() == null) {
            permissions = List.of();
        } else {
            permissions = role.getPermissions()
                .stream()
                .filter(Objects::nonNull)
                .filter(RolePermissionEntity::getGranted)
                .map(RolePermissionEntity::getResourcePermission)
                .map(this::toDomain)
                .filter(Objects::nonNull)
                .toList();
        }
        return Role.builder()
            .withName(role.getName())
            .withPermissions(permissions)
            .build();
    }

}
