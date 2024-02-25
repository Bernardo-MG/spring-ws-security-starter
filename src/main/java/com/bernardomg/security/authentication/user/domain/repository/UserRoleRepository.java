
package com.bernardomg.security.authentication.user.domain.repository;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.role.domain.model.Role;

public interface UserRoleRepository {

    /**
     * Returns all the roles available to the user.
     *
     * @param username
     *            user to search for
     * @param page
     *            pagination to apply
     * @return all the roles available to the user
     */
    public Iterable<Role> findAvailableToUser(final String username, final Pageable page);

}
