
package com.bernardomg.security.initializer.domain.model;

import java.util.Collection;

public record PermissionConfig(Collection<String> actions, Collection<ResourcePermission> permissions) {}
