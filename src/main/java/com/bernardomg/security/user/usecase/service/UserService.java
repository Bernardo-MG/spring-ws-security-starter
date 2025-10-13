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

package com.bernardomg.security.user.usecase.service;

import java.util.Optional;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.user.domain.model.User;
import com.bernardomg.security.user.domain.model.UserQuery;

/**
 * User service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public interface UserService {

    /**
     * Persists the received user.
     *
     * @param user
     *            user to create
     * @return the persisted user
     */
    public User create(final User user);

    /**
     * Deletes the user with the received id.
     *
     * @param username
     *            username of the user to delete
     * @return the deleted user
     */
    public User delete(final String username);

    /**
     * Returns all the users matching the sample in a paginated form. If the sample fields are empty, then all the users
     * are returned.
     *
     * @param query
     *            sample for filtering
     * @param pagination
     *            pagination to apply
     * @param sorting
     *            sorting to apply
     * @return a page for the users matching the sample
     */
    public Page<User> getAll(final UserQuery query, final Pagination pagination, final Sorting sorting);

    /**
     * Returns all the roles available to the user, in paginated form.
     *
     * @param username
     *            user username
     * @param pagination
     *            pagination to apply
     * @param sorting
     *            sorting to apply
     * @return a page with the available roles
     */
    public Page<Role> getAvailableRoles(final String username, final Pagination pagination, final Sorting sorting);

    /**
     * Returns the user for the received username, if it exists. Otherwise an empty {@code Optional} is returned.
     *
     * @param username
     *            username of the user to acquire
     * @return an {@code Optional} with the user, if it exists, of an empty {@code Optional} otherwise
     */
    public Optional<User> getOne(final String username);

    /**
     * Updates the received user with the received data.
     *
     * @param user
     *            new data for the user
     * @return the updated user
     */
    public User update(final User user);

}
