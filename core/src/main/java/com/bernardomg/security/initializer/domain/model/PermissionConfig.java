
package com.bernardomg.security.initializer.domain.model;

import java.util.Collection;
import java.util.List;

public final class PermissionConfig {

    private Collection<String>                   actions     = List.of();

    private Collection<ResourcePermissionConfig> permissions = List.of();

    public final Collection<String> getActions() {
        return actions;
    }

    public final Collection<ResourcePermissionConfig> getPermissions() {
        return permissions;
    }

    public final void setActions(final Collection<String> actions) {
        if (actions != null) {
            this.actions = actions;
        }
    }

    public final void setPermissions(final Collection<ResourcePermissionConfig> permissions) {
        if (permissions != null) {
            this.permissions = permissions;
        }
    }

}
