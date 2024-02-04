
package com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.domain.model.RolePermission;
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
    public final ResourcePermission addPermission(final RolePermission permission) {
        final RolePermissionEntity               rolePermission;
        final Optional<RoleEntity>               readRole;
        final Optional<ResourcePermissionEntity> readPermission;

        log.debug("Adding permission {} for role {}", permission.getPermission(), permission.getRole());

        readRole = roleRepository.findOneByName(permission.getRole());

        readPermission = resourcePermissionRepository.findByName(permission.getPermission());

        // Granted permission
        rolePermission = getRolePermissionSample(readRole.get()
            .getId(), permission.getPermission());
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
    public final Collection<ResourcePermission> findAll() {
        return resourcePermissionRepository.findAll()
            .stream()
            .map(this::toDomain)
            .distinct()
            .toList();
    }

    @Override
    public final Collection<ResourcePermission> findAllForUser(final String username) {
        final Optional<UserEntity>           user;
        final Collection<ResourcePermission> permissions;

        user = userRepository.findOneByUsername(username);
        if (user.isPresent()) {
            permissions = resourcePermissionRepository.findAllForUser(user.get()
                .getId())
                .stream()
                .map(this::toDomain)
                .distinct()
                .toList();
        } else {
            permissions = List.of();
        }

        return permissions;
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
    public final ResourcePermission removePermission(final RolePermission permission) {
        final RolePermissionEntity               rolePermissionSample;
        final Optional<ResourcePermissionEntity> readPermission;
        final Optional<RoleEntity>               readRole;

        log.debug("Removing permission {} for role {}", permission.getPermission(), permission.getRole());

        readRole = roleRepository.findOneByName(permission.getRole());

        readPermission = resourcePermissionRepository.findByName(permission.getPermission());

        // Not granted permission
        rolePermissionSample = getRolePermissionSample(readRole.get()
            .getId(), permission.getPermission());
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
