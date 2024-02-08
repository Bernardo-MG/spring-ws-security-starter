
package com.bernardomg.security.authorization.permission.domain.repository;

import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.domain.model.RolePermission;

public interface RolePermissionRepository {

    public ResourcePermission addPermission(final RolePermission permission);

    public boolean exists(final String role, final String permission);

    /**
     * Removes a permission from a role.
     *
     * @param permission
     *            permission to remove
     * @return the removed permission
     */
    public ResourcePermission removePermission(final RolePermission permission);

    public RolePermission save(final RolePermission permission);

}
