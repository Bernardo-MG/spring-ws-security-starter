
package com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository;

import com.bernardomg.security.authorization.permission.domain.repository.RolePermissionRepository;

public final class JpaRolePermissionRepository implements RolePermissionRepository {

    private final RolePermissionSpringRepository rolePermissionRepository;

    public JpaRolePermissionRepository(final RolePermissionSpringRepository rolePermissionRepo) {
        super();

        rolePermissionRepository = rolePermissionRepo;
    }

    @Override
    public final boolean exists(final String role, final String permission) {
        return rolePermissionRepository.existsByNameAndPermissionAndGranted(role, permission, true);
    }

}
