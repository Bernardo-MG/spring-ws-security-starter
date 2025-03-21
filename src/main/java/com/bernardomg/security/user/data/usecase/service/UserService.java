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

package com.bernardomg.security.user.data.usecase.service;

import java.util.Optional;

import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.user.data.domain.model.User;
import com.bernardomg.security.user.data.domain.model.UserQuery;

/**
 * User service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public interface UserService {

    /**
     * Deletes the user with the received id.
     *
     * @param username
     *            username of the user to delete
     */
    public void delete(final String username);

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
    public Iterable<User> getAll(final UserQuery query, final Pagination pagination, final Sorting sorting);

    /**
     * Returns the user for the received username, if it exists. Otherwise an empty {@code Optional} is returned.
     *
     * @param username
     *            username of the user to acquire
     * @return an {@code Optional} with the user, if it exists, of an empty {@code Optional} otherwise
     */
    public Optional<User> getOne(final String username);

    /**
     * Persists the received user.
     *
     * @param username
     *            username for the user to persist
     * @param name
     *            name for the user to persist
     * @param email
     *            email for the user to persist
     * @return the persisted user
     */
    public User registerNewUser(final String username, final String name, final String email);

    /**
     * Updates the received user with the received data.
     *
     * @param user
     *            new data for the user
     * @return the updated user
     */
    public User update(final User user);

}
