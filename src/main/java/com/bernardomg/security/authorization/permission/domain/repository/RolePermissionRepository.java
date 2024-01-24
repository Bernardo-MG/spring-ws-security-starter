
package com.bernardomg.security.authorization.permission.domain.repository;

import com.bernardomg.security.authorization.permission.domain.model.RolePermission;

public interface RolePermissionRepository {

    public boolean exists(final String role, final String permission);

    public RolePermission save(final RolePermission permission);

}
