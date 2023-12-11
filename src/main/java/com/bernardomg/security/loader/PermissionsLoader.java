
package com.bernardomg.security.loader;

import java.util.Collection;
import java.util.Objects;

import org.springframework.data.domain.Example;

import com.bernardomg.security.authorization.permission.persistence.model.ActionEntity;
import com.bernardomg.security.authorization.permission.persistence.model.ResourceEntity;
import com.bernardomg.security.authorization.permission.persistence.model.ResourcePermissionEntity;
import com.bernardomg.security.authorization.permission.persistence.repository.ActionRepository;
import com.bernardomg.security.authorization.permission.persistence.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.permission.persistence.repository.ResourceRepository;

public final class PermissionsLoader {

    private final ActionRepository               actionRepository;

    private final Collection<PermissionRegister> permissions;

    private final ResourcePermissionRepository   resourcePermissionRepository;

    private final ResourceRepository             resourceRepository;

    public PermissionsLoader(final ActionRepository actionRepo, final ResourceRepository resourceRepo,
            final ResourcePermissionRepository resourcePermissionRepo, final Collection<PermissionRegister> perms) {
        super();

        actionRepository = Objects.requireNonNull(actionRepo);
        resourceRepository = Objects.requireNonNull(resourceRepo);
        resourcePermissionRepository = Objects.requireNonNull(resourcePermissionRepo);
        permissions = Objects.requireNonNull(perms);
    }

    public final void load() {
        permissions.stream()
            .forEach(this::load);
    }

    private final void load(final PermissionRegister permissionRegister) {
        permissionRegister.getActions()
            .stream()
            .forEach(this::saveAction);
        permissionRegister.getResources()
            .stream()
            .forEach(this::saveResource);
        permissionRegister.getPermissions()
            .stream()
            .forEach(this::savePermission);
    }

    private void saveAction(final String action) {
        final Example<ActionEntity> example;
        final ActionEntity          actionEntity;

        actionEntity = ActionEntity.builder()
            .withName(action)
            .build();
        example = Example.of(actionEntity);
        if (!actionRepository.exists(example)) {
            actionRepository.save(actionEntity);
        }
    }

    private void savePermission(final ResourcePermissionPair pair) {
        final Example<ResourcePermissionEntity> example;
        final ResourcePermissionEntity          resourcePermissionEntity;

        resourcePermissionEntity = ResourcePermissionEntity.builder()
            .withName(pair.getAction() + ":" + pair.getResource())
            .withAction(pair.getAction())
            .withResource(pair.getResource())
            .build();
        example = Example.of(resourcePermissionEntity);
        if (!resourcePermissionRepository.exists(example)) {
            resourcePermissionRepository.save(resourcePermissionEntity);
        }
    }

    private void saveResource(final String resource) {
        final Example<ResourceEntity> example;
        final ResourceEntity          resourceEntity;

        resourceEntity = ResourceEntity.builder()
            .withName(resource)
            .build();
        example = Example.of(resourceEntity);
        if (!resourceRepository.exists(example)) {
            resourceRepository.save(resourceEntity);
        }
    }

}
