
package com.bernardomg.security.configuration;

import java.util.Collection;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

/**
 * Permission files properties.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 * @param files
 *            Files to load permissions from
 */
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
