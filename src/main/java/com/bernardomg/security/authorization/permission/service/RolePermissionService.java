
package com.bernardomg.security.authorization.permission.service;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.model.ResourcePermission;
import com.bernardomg.security.authorization.role.model.RolePermission;

public interface RolePermissionService {

    /**
     * Adds a action to a role.
     *
     * @param roleId
     *            role id
     * @param permission
     *            permission id to add
     * @return the added permission
     */
    public RolePermission addPermission(final long roleId, final Long permission);

    /**
     * Returns all permissions available to a role.
     *
     * @param roleId
     *            role id
     * @param pageable
     *            pagination to apply
     * @return permissions the role doesn't have
     */
    public Iterable<ResourcePermission> getAvailablePermissions(final long roleId, final Pageable pageable);

    /**
     * Returns all permissions assigned to a role.
     *
     * @param roleId
     *            role id
     * @param pageable
     *            pagination to apply
     * @return role permissions
     */
    public Iterable<ResourcePermission> getPermissions(final long roleId, final Pageable pageable);

    /**
     * Removes a action from a role.
     *
     * @param roleId
     *            role id
     * @param permission
     *            permission id to remove
     * @return the removed permission
     */
    public RolePermission removePermission(final long roleId, final Long permission);

}
