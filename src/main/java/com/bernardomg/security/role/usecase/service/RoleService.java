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

package com.bernardomg.security.role.usecase.service;

import java.util.Optional;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.permission.data.domain.model.ResourcePermission;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.domain.model.RoleQuery;

/**
 * User service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public interface RoleService {

    /**
     * Persists the received role.
     *
     * @param role
     *            role to create
     * @return the persisted role
     */
    public Role create(final Role role);

    /**
     * Deletes the role with the received id.
     *
     * @param role
     *            name of the role to delete
     * @return the deleted role
     */
    public Role delete(final String role);

    /**
     * Returns all the roles matching the sample. If the sample fields are empty, then all the roles are returned.
     *
     * @param sample
     *            sample for filtering
     * @param pagination
     *            pagination to apply
     * @param sorting
     *            sorting to apply
     * @return a page for the roles matching the sample
     */
    public Page<Role> getAll(final RoleQuery sample, final Pagination pagination, final Sorting sorting);

    /**
     * Returns all permissions available to a role in a paginated form.
     *
     * @param role
     *            role name
     * @param pagination
     *            pagination to apply
     * @param sorting
     *            sorting to apply
     * @return permissions the role doesn't have
     */
    public Page<ResourcePermission> getAvailablePermissions(final String role, final Pagination pagination,
            final Sorting sorting);

    /**
     * Returns the role for the received name, if it exists. Otherwise an empty {@code Optional} is returned.
     *
     * @param role
     *            name of the role to acquire
     * @return an {@code Optional} with the role, if it exists, of an empty {@code Optional} otherwise
     */
    public Optional<Role> getOne(final String role);

    /**
     * Updates the role for the received name with the received data.
     *
     * @param role
     *            role to update
     * @return the updated role
     */
    public Role update(final Role role);

}
