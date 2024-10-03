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

package com.bernardomg.security.permission.initializer.usecase;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import com.bernardomg.security.permission.data.domain.model.Action;
import com.bernardomg.security.permission.data.domain.model.Resource;
import com.bernardomg.security.permission.data.domain.model.ResourcePermission;
import com.bernardomg.security.permission.data.domain.repository.ActionRepository;
import com.bernardomg.security.permission.data.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.permission.data.domain.repository.ResourceRepository;

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
        final List<Action>             actions;
        final List<Resource>           resources;
        final List<ResourcePermission> permissions;

        log.debug("Begins loading permissions");

        // TODO: Apply transactionality
        // TODO: Group each into a single query

        // Load actions
        log.debug("Saving actions");
        actions = permissionRegisters.stream()
            .map(PermissionRegister::getActions)
            .flatMap(Collection::stream)
            .filter(Predicate.not(actionRepository::exists))
            .map(this::toAction)
            .toList();
        actionRepository.save(actions);
        log.debug("Saved actions");

        // Load resources
        log.debug("Saving resources");
        resources = permissionRegisters.stream()
            .map(PermissionRegister::getResources)
            .flatMap(Collection::stream)
            .filter(Predicate.not(resourceRepository::exists))
            .map(this::toResource)
            .toList();
        resourceRepository.save(resources);
        log.debug("Saved resources");

        // TODO: Verify the resources and actions exist
        // Load permissions
        log.debug("Saving permissions");
        permissions = permissionRegisters.stream()
            .map(PermissionRegister::getPermissions)
            .flatMap(Collection::stream)
            .map(this::toResourcePermission)
            .filter(Predicate.not(p -> resourcePermissionRepository.exists(p.getName())))
            .toList();
        resourcePermissionRepository.save(permissions);
        log.debug("Saved permissions");

        log.debug("Finished loading permissions");
    }

    private final Action toAction(final String name) {
        return new Action(name);
    }

    private final Resource toResource(final String name) {
        return new Resource(name);
    }

    private final ResourcePermission toResourcePermission(final ResourcePermissionPair pair) {
        return new ResourcePermission(pair.resource(), pair.action());
    }

}
