
package com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.authentication.user.domain.repository.UserRoleRepository;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.authorization.permission.domain.comparator.ResourcePermissionComparator;
import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleSpringRepository;
import com.bernardomg.security.authorization.role.domain.model.Role;

@Transactional
public final class JpaUserRoleRepository implements UserRoleRepository {

    private final RoleSpringRepository roleSpringRepository;

    public JpaUserRoleRepository(final RoleSpringRepository roleSpringRepo) {
        super();

        roleSpringRepository = Objects.requireNonNull(roleSpringRepo);
    }

    @Override
    public final Iterable<Role> findAvailableToUser(final String username, final Pageable page) {
        // TODO: this doesn't need the full role model, just the names
        return roleSpringRepository.findAllByUser(username, page)
            .map(this::toDomain);
    }

    private final ResourcePermission toDomain(final ResourcePermissionEntity entity) {
        return ResourcePermission.of(entity.getResource(), entity.getAction());
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
                .sorted(new ResourcePermissionComparator())
                .toList();
        }
        return Role.of(role.getName(), permissions);
    }

}
