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

package com.bernardomg.security.authentication.user.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.model.UserQuery;

/**
 * User repository.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
public interface UserRepository {

    /**
     * Deletes the user with the received username.
     *
     * @param username
     *            user to delete
     */
    public void delete(final String username);

    /**
     * Returns whether an user with the given username exists.
     *
     * @param username
     *            username to search for
     * @return {@code true} if the user exists, {@code false} otherwise
     */
    public boolean exists(final String username);

    /**
     * Returns whether an user with the given email exists.
     *
     * @param email
     *            email to search for
     * @return {@code true} if the user exists, {@code false} otherwise
     */
    public boolean existsByEmail(final String email);

    /**
     * Returns whether an user, other than the one received, has the email.
     *
     * @param username
     *            user to ignore
     * @param email
     *            email to search for
     * @return {@code true} if another user has the email, {@code false} otherwise
     */
    public boolean existsEmailForAnotherUser(final String username, final String email);

    /**
     * Returns all the users for the received query.
     *
     * @param query
     *            query to filter the users
     * @param page
     *            pagination to apply
     * @return all the user for the received query
     */
    public Iterable<User> findAll(final UserQuery query, final Pageable page);

    /**
     * Returns the user for the received username.
     *
     * @param username
     *            user to search for
     * @return the user for the received username
     */
    public Optional<User> findOne(final String username);

    /**
     * Returns the user for the received email.
     *
     * @param email
     *            email to search for
     * @return the user for the received email
     */
    public Optional<User> findOneByEmail(final String email);

    /**
     * Returns the password for the user.
     *
     * @param username
     *            user to search for the password
     * @return the user password
     */
    public Optional<String> findPassword(final String username);

    /**
     * Saves the received user. If it exists it is updated, otherwise it is created.
     *
     * @param user
     *            user to save
     * @param password
     *            password for the user
     * @return the saved user
     */
    public User save(final User user, final String password);

    /**
     * Updates an existing user.
     *
     * @param user
     *            the user to update
     * @return the updated user
     */
    public User update(final User user);

}
