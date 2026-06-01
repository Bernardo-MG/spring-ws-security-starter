
package com.bernardomg.security.initializer.domain.model;

import java.util.Collection;

public final class ResourcePermissionConfig {

    private Collection<String> actions;

    private String             resource;

    public Collection<String> getActions() {
        return actions;
    }

    public String getResource() {
        return resource;
    }

    public void setActions(final Collection<String> actions) {
        this.actions = actions;
    }

    public void setResource(final String resource) {
        this.resource = resource;
    }

}
