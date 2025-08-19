/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023-2025 the original author or authors.
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

package com.bernardomg.security.role.domain.repository;

import java.util.Optional;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.domain.model.RoleQuery;

/**
 * Role repository.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
public interface RoleRepository {

    /**
     * Deletes the role with the received name.
     *
     * @param name
     *            role to delete
     */
    public void delete(final String name);

    /**
     * Checks if a role exists with the given name.
     *
     * @param name
     *            name of the role to check
     * @return {@code true} if the role exists, {@code false} otherwise
     */
    public boolean exists(final String name);

    /**
     * Returns all the roles for the received query.
     *
     * @param query
     *            query to filter the roles
     * @param pagination
     *            pagination to apply
     * @param sorting
     *            sorting to apply
     * @return all the roles for the received query
     */
    public Page<Role> findAll(final RoleQuery query, final Pagination pagination, final Sorting sorting);

    /**
     * Returns the role for the received name.
     *
     * @param name
     *            role to search for
     * @return the role for the received name
     */
    public Optional<Role> findOne(final String name);

    /**
     * Checks if a role exists with the given name for any user.
     *
     * @param name
     *            name of the role to check
     * @return {@code true} if the role exists for any user, {@code false} otherwise
     */
    public boolean isLinkedToUser(final String name);

    /**
     * Saves the received role. If it exists it is updated, otherwise it is created.
     *
     * @param role
     *            role to save
     * @return the saved user
     */
    public Role save(final Role role);

}
