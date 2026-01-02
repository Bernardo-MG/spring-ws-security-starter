/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023-2025 the original author or authors.
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

package com.bernardomg.security.initializer.usecase.loader;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.bernardomg.security.initializer.domain.model.PermissionConfig;
import com.bernardomg.security.initializer.domain.model.ResourcePermissionConfig;
import com.bernardomg.security.permission.domain.model.Action;
import com.bernardomg.security.permission.domain.model.Resource;
import com.bernardomg.security.permission.domain.model.ResourcePermission;
import com.bernardomg.security.permission.domain.repository.ActionRepository;
import com.bernardomg.security.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.permission.domain.repository.ResourceRepository;

/**
 * Loads the permissions configuration for the application. These are loaded from a list of {@link PermissionRegister}.
 * <p>
 * The {@link #load()} method takes care of persisting all the data.
 */
@Transactional
public final class PermissionsLoader implements Loader {

    /**
     * Logger for the class.
     */
    private static final Logger                log = LoggerFactory.getLogger(PermissionsLoader.class);

    /**
     * Actions repository.
     */
    private final ActionRepository             actionRepository;

    /**
     * Permissions to load.
     */
    private final List<PermissionConfig>       permissionConfigs;

    /**
     * Resource permissions repository.
     */
    private final ResourcePermissionRepository resourcePermissionRepository;

    /**
     * Resource repository.
     */
    private final ResourceRepository           resourceRepository;

    public PermissionsLoader(final ActionRepository actionRepo, final ResourceRepository resourceRepo,
            final ResourcePermissionRepository resourcePermissionRepo, final Collection<InputStream> permissions) {
        super();

        actionRepository = Objects.requireNonNull(actionRepo);
        resourceRepository = Objects.requireNonNull(resourceRepo);
        resourcePermissionRepository = Objects.requireNonNull(resourcePermissionRepo);

        permissionConfigs = permissions.stream()
            .map(this::readPermissions)
            .toList();
    }

    /**
     * Persists the permissions.
     */
    @Override
    public final void load() {
        final List<Action>       actions;
        final List<Resource>     resources;
        final Collection<String> actionNames;
        final Collection<String> resourceNames;

        log.debug("Begins loading permissions");

        // TODO: Load default actions
        // Load actions
        log.debug("Saving actions");
        actionNames = actionRepository.findAllNames();
        actions = permissionConfigs.stream()
            .map(PermissionConfig::getActions)
            .flatMap(Collection::stream)
            .map(String::toUpperCase)
            .distinct()
            .filter(Predicate.not(actionNames::contains))
            .map(Action::new)
            .toList();
        actionRepository.save(actions);
        log.debug("Saved actions");

        // Load resources
        // TODO: maybe they should be defined elsewhere like the actions
        log.debug("Saving resources");
        resourceNames = resourceRepository.findAllNames();
        resources = permissionConfigs.stream()
            .map(PermissionConfig::getPermissions)
            .flatMap(Collection::stream)
            .map(ResourcePermissionConfig::getResource)
            .map(String::toUpperCase)
            .distinct()
            .filter(Predicate.not(resourceNames::contains))
            .map(Resource::new)
            .toList();
        resourceRepository.save(resources);
        log.debug("Saved resources");

        // Load permissions
        loadPermissions();

        log.debug("Finished loading permissions");
    }

    private final void loadPermissions() {
        final List<ResourcePermission> permissions;
        final Collection<String>       actionNames;
        final Collection<String>       resourceNames;
        final Collection<String>       permissionNames;

        log.debug("Saving permissions");
        actionNames = actionRepository.findAllNames();
        resourceNames = resourceRepository.findAllNames();
        permissionNames = resourcePermissionRepository.findAll()
            .stream()
            .map(this::toName)
            .toList();
        permissions = permissionConfigs.stream()
            .map(PermissionConfig::getPermissions)
            .flatMap(Collection::stream)
            .map(this::toResourcePermission)
            .flatMap(Collection::stream)
            .distinct()
            .filter(p -> actionNames.contains(p.action()))
            .filter(p -> resourceNames.contains(p.resource()))
            .filter(p -> !permissionNames.contains(toName(p)))
            .toList();
        resourcePermissionRepository.save(permissions);
        log.debug("Saved permissions");
    }

    private final PermissionConfig readPermissions(final InputStream permissions) {
        final Yaml       yaml;
        PermissionConfig config;

        yaml = new Yaml(new Constructor(PermissionConfig.class, new LoaderOptions()));
        config = yaml.load(permissions);
        if (config == null) {
            config = new PermissionConfig();
        }

        return config;
    }

    private final String toName(final ResourcePermission permission) {
        return String.format("%s:%s", permission.resource(), permission.action());
    }

    private final Collection<ResourcePermission> toResourcePermission(final ResourcePermissionConfig config) {
        return config.getActions()
            .stream()
            .map(a -> new ResourcePermission(config.getResource()
                .toUpperCase(), a.toUpperCase()))
            .toList();
    }

}
