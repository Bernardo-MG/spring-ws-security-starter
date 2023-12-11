
package com.bernardomg.security.loader;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(setterPrefix = "with")
public final class ResourcePermissionPair {

    public static final ResourcePermissionPair of(final String action, final String resource) {
        return ResourcePermissionPair.builder()
            .withAction(action)
            .withResource(resource)
            .build();
    }

    private final String action;

    private final String resource;

}
