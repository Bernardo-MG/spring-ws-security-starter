
package com.bernardomg.security.adapter.inbound.spring.initializer.loader;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.bernardomg.security.usecase.initializer.domain.model.PermissionConfig;
import com.bernardomg.security.usecase.initializer.loader.PermissionConfigLoader;

public final class SpringResourcePermissionConfigLoader implements PermissionConfigLoader {

    /**
     * Logger for the class.
     */
    private static final Logger        log = LoggerFactory.getLogger(SpringResourcePermissionConfigLoader.class);

    private final Collection<Resource> permissionResources;

    public SpringResourcePermissionConfigLoader(final Collection<Resource> permissionResources) {

        super();

        this.permissionResources = List.copyOf(Objects.requireNonNull(permissionResources));
    }

    @Override
    public final Collection<PermissionConfig> load() {
        return permissionResources.stream()
            .map(this::readPermissions)
            .toList();
    }

    private PermissionConfig readPermissions(final Resource resource) {

        final Yaml       yaml;
        PermissionConfig config;

        if (!resource.exists()) {
            log.error("Missing permissions resource {}", resource);

            throw new UncheckedIOException(new IOException("Missing permissions resource " + resource));
        }

        try (final InputStream input = resource.getInputStream()) {

            yaml = new Yaml(new Constructor(PermissionConfig.class, new LoaderOptions()));

            config = yaml.load(input);
        } catch (final IOException e) {
            log.error("Failed to read permissions resource {}", resource, e);

            throw new UncheckedIOException("Failed to read permissions resource " + resource, e);
        }

        if (config == null) {
            config = new PermissionConfig();
        }

        return config;
    }
}
