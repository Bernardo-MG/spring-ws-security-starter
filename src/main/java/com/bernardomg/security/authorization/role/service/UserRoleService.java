/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bernardomg.security.authorization.role.service;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.model.UserRole;

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
