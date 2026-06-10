
package com.bernardomg.security.initializer.configuration;

import java.util.Collection;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "security.permissions")
public record PermissionsFilesProperties(Collection<Resource> files) {

    public PermissionsFilesProperties(final Collection<Resource> files) {
        if (files == null) {
            this.files = List.of();
        } else {
            this.files = files;
        }
    }

}
