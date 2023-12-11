
package com.bernardomg.security.loader;

import java.util.Collection;

public interface PermissionRegister {

    public Collection<String> getActions();

    public Collection<ResourcePermissionPair> getPermissions();

    public Collection<String> getResources();

}
