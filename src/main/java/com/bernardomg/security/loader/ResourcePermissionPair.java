
package com.bernardomg.security.loader;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(setterPrefix = "with")
public final class ResourcePermissionPair {

    private final String action;

    private final String resource;

}
