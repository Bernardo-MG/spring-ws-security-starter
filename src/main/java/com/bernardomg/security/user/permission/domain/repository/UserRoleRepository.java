
package com.bernardomg.security.user.permission.domain.repository;

import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.role.domain.model.Role;

public interface UserRoleRepository {

    /**
     * Returns all the roles available to the user.
     *
     * @param username
     *            user to search for
     * @param pagination
     *            pagination to apply
     * @param sorting
     *            sorting to apply
     * @return all the roles available to the user
     */
    public Iterable<Role> findAvailableToUser(final String username, final Pagination pagination,
            final Sorting sorting);

}
