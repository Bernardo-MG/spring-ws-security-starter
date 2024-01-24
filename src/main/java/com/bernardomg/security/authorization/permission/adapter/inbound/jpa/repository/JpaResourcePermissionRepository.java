
package com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleSpringRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JpaResourcePermissionRepository implements ResourcePermissionRepository {

    /**
     * Resource permissions repository. Used not only to return the permissions, but also to validate they exist.
     */
    private final ResourcePermissionSpringRepository resourcePermissionRepository;

    /**
     * Role permissions repository. Used to modify permissions for the roles.
     */
    private final RolePermissionSpringRepository     rolePermissionRepository;

    /**
     * Role repository. Used to validate the role exists.
     */
    private final RoleSpringRepository               roleRepository;

    private final UserSpringRepository               userRepository;

    public JpaResourcePermissionRepository(final UserSpringRepository userRepo,
            final ResourcePermissionSpringRepository resourcePermissionRepo,
            final RolePermissionSpringRepository rolePermissionRepo, final RoleSpringRepository roleRepo) {
        super();

        userRepository = userRepo;
        resourcePermissionRepository = resourcePermissionRepo;
        rolePermissionRepository = rolePermissionRepo;
        roleRepository = roleRepo;
    }

    @Override
    public final ResourcePermission addPermission(final String role, final String permission) {
        final RolePermissionEntity               rolePermission;
        final Optional<RoleEntity>               readRole;
        final Optional<ResourcePermissionEntity> readPermission;

        log.debug("Adding permission {} for role {}", permission, role);

        readRole = roleRepository.findOneByName(role);

        readPermission = resourcePermissionRepository.findByName(permission);

        // Granted permission
        rolePermission = getRolePermissionSample(readRole.get()
            .getId(), permission);
        rolePermission.setGranted(true);

        // Persist relationship entities
        rolePermissionRepository.save(rolePermission);

        return readPermission.map(this::toDomain)
            .get();
    }

    @Override
    public final boolean exists(final String name) {
        return resourcePermissionRepository.existsByName(name);
    }

    @Override
    public final Collection<ResourcePermission> findAllForUser(final String username) {
        final Optional<UserEntity> user;

        user = userRepository.findOneByUsername(username);

        return resourcePermissionRepository.findAllForUser(user.get()
            .getId())
            .stream()
            .map(this::toDomain)
            .distinct()
            .toList();
    }

    @Override
    public final Iterable<ResourcePermission> findAvailablePermissions(final String role, final Pageable pageable) {
        final Optional<RoleEntity> readRole;

        log.debug("Reading available permissions for {}", role);

        readRole = roleRepository.findOneByName(role);

        return resourcePermissionRepository.findAllAvailableToRole(readRole.get()
            .getId(), pageable)
            .map(this::toDomain);
    }

    @Override
    public final Iterable<ResourcePermission> findPermissionsForRole(final String role, final Pageable page) {
        final Optional<RoleEntity> readRole;

        log.debug("Reading permissions for {}", role);

        readRole = roleRepository.findOneByName(role);

        return resourcePermissionRepository.findAllForRole(readRole.get()
            .getId(), page)
            .map(this::toDomain);
    }

    @Override
    public final ResourcePermission removePermission(final String role, final String permission) {
        final RolePermissionEntity               rolePermissionSample;
        final Optional<ResourcePermissionEntity> readPermission;
        final Optional<RoleEntity>               readRole;

        log.debug("Removing permission {} for role {}", permission, role);

        readRole = roleRepository.findOneByName(role);

        readPermission = resourcePermissionRepository.findByName(permission);

        // Not granted permission
        rolePermissionSample = getRolePermissionSample(readRole.get()
            .getId(), permission);
        rolePermissionSample.setGranted(false);

        // Delete relationship entities
        rolePermissionRepository.save(rolePermissionSample);

        return toDomain(readPermission.get());
    }

    @Override
    public final ResourcePermission save(final ResourcePermission permission) {
        final Optional<ResourcePermissionEntity> existing;
        final ResourcePermissionEntity           entity;
        final ResourcePermissionEntity           created;

        log.debug("Saving permission {}", permission);

        entity = toEntity(permission);

        existing = resourcePermissionRepository.findByName(permission.getName());
        if (existing.isPresent()) {
            entity.setId(existing.get()
                .getId());
        }

        created = resourcePermissionRepository.save(entity);

        return toDomain(created);
    }

    private final RolePermissionEntity getRolePermissionSample(final long roleId, final String permission) {
        return RolePermissionEntity.builder()
            .withRoleId(roleId)
            .withPermission(permission)
            .build();
    }

    private final ResourcePermission toDomain(final ResourcePermissionEntity entity) {
        return ResourcePermission.builder()
            .withName(entity.getName())
            .withResource(entity.getResource())
            .withAction(entity.getAction())
            .build();
    }

    private final ResourcePermissionEntity toEntity(final ResourcePermission entity) {
        return ResourcePermissionEntity.builder()
            .withName(entity.getName())
            .withResource(entity.getResource())
            .withAction(entity.getAction())
            .build();
    }

}
