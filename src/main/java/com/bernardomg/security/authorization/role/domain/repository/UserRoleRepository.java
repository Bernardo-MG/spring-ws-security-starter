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

package com.bernardomg.security.authorization.role.domain.repository;

/**
 * User role repository.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
public interface UserRoleRepository {

    /**
     * Deletes the role for the user.
     *
     * @param name
     *            role to delete
     */
    public void delete(final String username, final String role);

    /**
     * Checks if a role exists with the given name for any user.
     *
     * @param role
     *            name of the role to check
     * @return {@code true} if the role exists for any user, {@code false} otherwise
     */
    public boolean existsForRole(final String role);

    /**
     * Saves the received role. If it exists it is updated, otherwise it is created.
     *
     * @param username
     *            username to assign the role to
     * @param role
     *            role to assign
     */
    public void save(final String username, final String role);

}
