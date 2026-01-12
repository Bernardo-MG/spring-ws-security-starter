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

package com.bernardomg.security.user.domain.repository;

import java.util.Optional;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.user.domain.model.User;
import com.bernardomg.security.user.domain.model.UserQuery;

/**
 * User repository.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
public interface UserRepository {

    /**
     * Activates the received user, setting the password and enabling it.
     *
     * @param username
     *            user to activate
     * @param password
     *            password for the user
     * @return the activated user
     */
    public User activate(final String username, final String password);

    /**
     * Removes all login attempts for the user.
     *
     * @param username
     *            user to remove login attempts
     */
    public void clearLoginAttempts(final String username);

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
     * @param pagination
     *            pagination to apply
     * @param sorting
     *            sorting to apply
     * @return all the user for the received query
     */
    public Page<User> findAll(final UserQuery query, final Pagination pagination, final Sorting sorting);

    /**
     * Returns the number of login attempts which the user has done.
     *
     * @param username
     *            user to search for the login attempts
     * @return number of login attempts for the user
     */
    public int findLoginAttempts(final String username);

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
     * Increases the number of login attempts and returns the new number.
     *
     * @param username
     *            user to increase the login attempts
     * @return number of login attempts for the user, if it doesn't exist then the value is negative
     */
    public int increaseLoginAttempts(final String username);

    /**
     * Locks the received user.
     *
     * @param username
     *            user to lock
     * @return the locked user
     */
    public User lock(final String username);

    /**
     * Resets the password for the user, this includes disabling the password expired flag.
     *
     * @param username
     *            user to refresh the password
     * @param password
     *            new password
     * @return the user with the refreshed password
     */
    public User resetPassword(final String username, final String password);

    /**
     * Updates an existing user.
     *
     * @param user
     *            the user to update
     * @return the updated user
     */
    public User save(final User user);

    /**
     * Creates a new user. It won't have a password, and will have the password expired flag active.
     *
     * @param user
     *            user to save
     * @param password
     *            user password
     * @return the saved user
     */
    public User save(final User user, final String password);

}
