
package com.bernardomg.security.loader;

import java.util.Collection;
import java.util.List;

public final class DefaultPermissionRegister implements PermissionRegister {

    @Override
    public final Collection<String> getActions() {
        return List.of("CREATE", "READ", "UPDATE", "DELETE", "VIEW");
    }

    @Override
    public final Collection<ResourcePermissionPair> getPermissions() {
        return List.of(ResourcePermissionPair.of("USER", "CREATE"), ResourcePermissionPair.of("USER", "READ"),
            ResourcePermissionPair.of("USER", "UPDATE"), ResourcePermissionPair.of("USER", "DELETE"),
            ResourcePermissionPair.of("ROLE", "CREATE"), ResourcePermissionPair.of("ROLE", "READ"),
            ResourcePermissionPair.of("ROLE", "UPDATE"), ResourcePermissionPair.of("ROLE", "DELETE"),
            ResourcePermissionPair.of("LOGIN_REGISTER", "READ"), ResourcePermissionPair.of("USER_TOKEN", "READ"),
            ResourcePermissionPair.of("USER_TOKEN", "UPDATE"), ResourcePermissionPair.of("USER_TOKEN", "DELETE"),
            ResourcePermissionPair.of("USER", "VIEW"), ResourcePermissionPair.of("ROLE", "VIEW"),
            ResourcePermissionPair.of("USER_TOKEN", "VIEW"));
    }

    @Override
    public final Collection<String> getResources() {
        return List.of("USER", "ROLE", "USER_TOKEN", "LOGIN_REGISTER");
    }

}
