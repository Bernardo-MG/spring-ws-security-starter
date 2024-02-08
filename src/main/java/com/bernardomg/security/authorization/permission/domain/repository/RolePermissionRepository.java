
package com.bernardomg.security.authorization.permission.domain.repository;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.domain.model.RolePermission;

public interface RolePermissionRepository {

    public RolePermission addPermission(final RolePermission permission);

    public boolean exists(final String role, final String permission);

    /**
     * Returns all the resource permissions available to a role.
     *
     * @param role
     *            role to search for the available permissions
     * @param pageable
     *            pagination to apply
     * @return all the resource permissions available to a role
     */
    public Iterable<ResourcePermission> findAvailablePermissions(final String role, final Pageable pageable);

    /**
     * Returns all the resource permissions for a role.
     *
     * @param role
     *            role to search for the permissions
     * @param pageable
     *            pagination to apply
     * @return all the resource permissions for the role
     */
    public Iterable<ResourcePermission> findPermissions(final String role, final Pageable page);

    /**
     * Removes a permission from a role.
     *
     * @param permission
     *            permission to remove
     * @return the removed permission
     */
    public RolePermission removePermission(final RolePermission permission);

    public RolePermission save(final RolePermission permission);

}
