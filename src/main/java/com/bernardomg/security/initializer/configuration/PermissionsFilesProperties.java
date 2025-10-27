
package com.bernardomg.security.initializer.configuration;

import java.util.Collection;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "security.permissions")
public record PermissionsFilesProperties(Collection<Resource> files) {

}
