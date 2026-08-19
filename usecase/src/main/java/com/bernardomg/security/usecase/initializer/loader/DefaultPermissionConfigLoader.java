
package com.bernardomg.security.usecase.initializer.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.bernardomg.security.usecase.initializer.domain.model.PermissionConfig;

public final class DefaultPermissionConfigLoader implements PermissionConfigLoader {

    /**
     * Logger for the class.
     */
    private static final Logger    log = LoggerFactory.getLogger(DefaultPermissionConfigLoader.class);

    private final Collection<File> permissionFiles;

    public DefaultPermissionConfigLoader(final Collection<File> permissionFiles) {

        this.permissionFiles = List.copyOf(Objects.requireNonNull(permissionFiles));
    }

    @Override
    public final Collection<PermissionConfig> load() {
        return permissionFiles.stream()
            .map(this::readPermissions)
            .toList();
    }

    private PermissionConfig readPermissions(final File file) {
        final Yaml       yaml;
        PermissionConfig config;

        if (!file.isFile()) {
            log.error("Missing permissions file {}", file);
            throw new UncheckedIOException(new IOException("Missing permissions file " + file));
        }

        try (final InputStream input = Files.newInputStream(file.toPath())) {
            yaml = new Yaml(new Constructor(PermissionConfig.class, new LoaderOptions()));

            config = yaml.load(input);
        } catch (final IOException e) {
            log.error("Failed to read permissions file {}", file);
            throw new UncheckedIOException("Failed to read permissions file " + file, e);
        }

        if (config == null) {
            config = new PermissionConfig();
        }

        return config;
    }
}
