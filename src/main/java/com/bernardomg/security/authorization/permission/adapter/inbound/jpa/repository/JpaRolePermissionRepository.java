
package com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository;

import java.util.Optional;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.domain.model.RolePermission;
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

    @Override
    public final RolePermission save(final RolePermission permission) {
        final RolePermissionEntity rolePermission;
        final Optional<RoleEntity> role;
        final RolePermissionEntity saved;

        role = roleRepository.findOneByName(permission.getRole());

        rolePermission = RolePermissionEntity.builder()
            .withRoleId(role.get()
                .getId())
            .withPermission(permission.getPermission())
            .withGranted(true)
            .build();
        saved = rolePermissionRepository.save(rolePermission);

        return toDomain(saved, role.get());
    }

    private final RolePermission toDomain(final RolePermissionEntity permission, final RoleEntity role) {
        return RolePermission.builder()
            .withPermission(permission.getPermission())
            .withRole(role.getName())
            .build();
    }

}
