
package com.bernardomg.security.initializer.usecase.loader;

import java.io.InputStream;

import org.yaml.snakeyaml.Yaml;

import com.bernardomg.security.initializer.domain.model.PermissionConfig;

public final class PermissionFileLoader {

    private final PermissionConfig permissionConfig;

    public PermissionFileLoader(final InputStream permissions) {
        final Yaml yaml = new Yaml();
        permissionConfig = yaml.load(permissions);
    }

    public PermissionConfig getPermissionConfig() {
        return permissionConfig;
    }
}
