/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bernardomg.security.authorization.permission.adapter.inbound.initializer;

import java.util.Collection;
import java.util.Objects;

import com.bernardomg.security.authorization.permission.domain.model.Action;
import com.bernardomg.security.authorization.permission.domain.model.Resource;
import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.domain.repository.ActionRepository;
import com.bernardomg.security.authorization.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.permission.domain.repository.ResourceRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Loads the permissions configuration for the application. These are loaded from a list of {@link PermissionRegister}.
 * <p>
 * The {@link #load()} method takes care of persisting all the data.
 */
@Slf4j
public final class PermissionsLoader {

    /**
     * Actions repository.
     */
    private final ActionRepository               actionRepository;

    /**
     * Permissions to load.
     */
    private final Collection<PermissionRegister> permissionRegisters;

    /**
     * Resource permissions repository.
     */
    private final ResourcePermissionRepository   resourcePermissionRepository;

    /**
     * Resource repository.
     */
    private final ResourceRepository             resourceRepository;

    public PermissionsLoader(final ActionRepository actionRepo, final ResourceRepository resourceRepo,
            final ResourcePermissionRepository resourcePermissionRepo, final Collection<PermissionRegister> perms) {
        super();

        actionRepository = Objects.requireNonNull(actionRepo);
        resourceRepository = Objects.requireNonNull(resourceRepo);
        resourcePermissionRepository = Objects.requireNonNull(resourcePermissionRepo);
        permissionRegisters = Objects.requireNonNull(perms);
    }

    /**
     * Persists the permissions.
     */
    public final void load() {
        log.debug("Begins loading permissions");

        // Load actions
        log.debug("Saving actions");
        permissionRegisters.stream()
            .map(PermissionRegister::getActions)
            .flatMap(Collection::stream)
            .forEach(this::saveAction);
        log.debug("Saved actions");

        // Load resources
        log.debug("Saving resources");
        permissionRegisters.stream()
            .map(PermissionRegister::getResources)
            .flatMap(Collection::stream)
            .forEach(this::saveResource);
        log.debug("Saved resources");

        // Load permissions
        log.debug("Saving permissions");
        permissionRegisters.stream()
            .map(PermissionRegister::getPermissions)
            .flatMap(Collection::stream)
            .forEach(this::savePermission);
        log.debug("Saved permissions");

        log.debug("Finished loading permissions");
    }

    private final void saveAction(final String name) {
        final Action action;

        action = Action.builder()
            .withName(name)
            .build();
        if (!actionRepository.exists(name)) {
            log.debug("Saving action {}", name);
            actionRepository.save(action);
        }
    }

    private final void savePermission(final ResourcePermissionPair pair) {
        final ResourcePermission resourcePermission;
        final String             name;

        resourcePermission = ResourcePermission.builder()
            .withName(pair.getAction() + ":" + pair.getResource())
            .withAction(pair.getAction())
            .withResource(pair.getResource())
            .build();
        // TODO: generate the name automatically
        name = pair.getAction() + ":" + pair.getResource();
        if (!resourcePermissionRepository.exists(name)) {
            log.debug("Saving permission {}:{}", pair.getResource(), pair.getAction());
            resourcePermissionRepository.save(resourcePermission);
        }
    }

    private final void saveResource(final String resource) {
        final Resource resourceData;

        resourceData = Resource.builder()
            .withName(resource)
            .build();
        if (!resourceRepository.exists(resource)) {
            log.debug("Saving resource {}", resource);
            resourceRepository.save(resourceData);
        }
    }

}
