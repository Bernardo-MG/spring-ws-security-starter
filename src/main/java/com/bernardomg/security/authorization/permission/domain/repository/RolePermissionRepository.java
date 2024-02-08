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

package com.bernardomg.security.authorization.permission.domain.repository;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.domain.model.RolePermission;

/**
 * Role permission repository.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
public interface RolePermissionRepository {

    /**
     * Adds a permission to a role.
     *
     * @param permission
     *            permission to add
     * @return the added permission
     */
    public RolePermission addPermission(final RolePermission permission);

    /**
     * Checks if the given permission exists.
     *
     * @param role
     *            permission role
     * @param permission
     *            permission name
     * @return {@code true} if the permission exists, {@code false} otherwise
     */
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
     * Returns all the resource permissions assigned to a role.
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

}
