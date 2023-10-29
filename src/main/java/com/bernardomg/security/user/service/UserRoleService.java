
package com.bernardomg.security.user.service;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.user.model.Role;
import com.bernardomg.security.user.model.UserRole;

public interface UserRoleService {

    /**
     * Adds a role to a user.
     *
     * @param userId
     *            user id
     * @param roleId
     *            role id to add
     * @return the added role
     */
    public UserRole addRole(final long userId, final long roleId);

    public Iterable<Role> getAvailableRoles(final long userId, final Pageable pageable);

    /**
     * Returns all the roles for the user.
     *
     * @param userId
     *            user id
     * @param pageable
     *            pagination to apply
     * @return roles for the rules
     */
    public Iterable<Role> getRoles(final long userId, final Pageable pageable);

    /**
     * Removes a role from a user.
     *
     * @param userId
     *            user id
     * @param roleId
     *            role id to remove
     * @return the removed role
     */
    public UserRole removeRole(final long userId, final long roleId);

}
