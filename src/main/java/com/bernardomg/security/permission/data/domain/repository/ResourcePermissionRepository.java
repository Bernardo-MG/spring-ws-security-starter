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

package com.bernardomg.security.permission.data.domain.repository;

import java.util.Collection;

import com.bernardomg.security.permission.data.domain.model.ResourcePermission;

/**
 * Resource permission repository.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
public interface ResourcePermissionRepository {

    /**
     * Checks if a resource permission exists with the given name.
     *
     * @param name
     *            name of the resource permission to check
     * @return {@code true} if the resource permission exists, {@code false} otherwise
     */
    public boolean exists(final String name);

    /**
     * Returns all the resource permissions.
     *
     * @return all the resource permissions
     */
    public Collection<ResourcePermission> findAll();

    /**
     * Returns the names of all the resources.
     *
     * @return the names of all the resources
     */
    public Collection<String> findAllNames();

    /**
     * Saves the received permissions. If they exist these are updated, otherwise they are created.
     *
     * @param permissions
     *            permissions to save
     * @return the saved permission
     */
    public Collection<ResourcePermission> save(final Collection<ResourcePermission> permissions);

}
