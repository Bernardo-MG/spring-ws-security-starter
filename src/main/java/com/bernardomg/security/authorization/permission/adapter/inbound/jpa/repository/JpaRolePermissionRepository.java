
package com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository;

import java.util.Optional;

import com.bernardomg.security.authorization.permission.domain.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleSpringRepository;

public final class JpaRolePermissionRepository implements RolePermissionRepository {

    private final RolePermissionSpringRepository rolePermissionRepository;

    private final RoleSpringRepository           roleRepository;

    public JpaRolePermissionRepository(final RoleSpringRepository roleRepo,
            final RolePermissionSpringRepository rolePermissionRepo) {
        super();

        roleRepository = roleRepo;
        rolePermissionRepository = rolePermissionRepo;
    }

    @Override
    public final boolean exists(final String role, final String permission) {
        final Optional<RoleEntity> readRole;
        final long                 id;

        readRole = roleRepository.findOneByName(role);
        id = readRole.get()
            .getId();
        return rolePermissionRepository.existsByRoleIdAndPermissionAndGranted(id, permission, true);
    }

}
