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

package com.bernardomg.security.authorization.permission.service;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.model.ResourcePermission;

/**
 * Role permissions service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public interface RolePermissionService {

    /**
     * Adds a permission to a role.
     *
     * @param role
     *            role name
     * @param permission
     *            permission to add
     * @return the added permission
     */
    public ResourcePermission addPermission(final String role, final String permission);

    /**
     * Returns all permissions available to a role in a paginated form.
     *
     * @param role
     *            role name
     * @param page
     *            pagination to apply
     * @return permissions the role doesn't have
     */
    public Iterable<ResourcePermission> getAvailablePermissions(final String role, final Pageable page);

    /**
     * Returns all permissions assigned to a role.
     *
     * @param role
     *            role name
     * @param page
     *            pagination to apply
     * @return role permissions
     */
    public Iterable<ResourcePermission> getPermissions(final String role, final Pageable page);

    /**
     * Removes a permission from a role.
     *
     * @param role
     *            role name
     * @param permission
     *            permission to remove
     * @return the removed permission
     */
    public ResourcePermission removePermission(final String role, final String permission);

}
