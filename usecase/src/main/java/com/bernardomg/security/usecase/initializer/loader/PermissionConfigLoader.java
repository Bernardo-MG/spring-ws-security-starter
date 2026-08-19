
package com.bernardomg.security.usecase.initializer.loader;

import java.util.Collection;

import com.bernardomg.security.usecase.initializer.domain.model.PermissionConfig;

public interface PermissionConfigLoader {

    public Collection<PermissionConfig> load();

}
