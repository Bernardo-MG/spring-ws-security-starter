
package com.bernardomg.security.loader;

import java.util.Collection;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(setterPrefix = "with")
public final class PermissionRegister {

    private final Collection<String>                 actions;

    private final Collection<ResourcePermissionPair> permissions;

    private final Collection<String>                 resources;

}
