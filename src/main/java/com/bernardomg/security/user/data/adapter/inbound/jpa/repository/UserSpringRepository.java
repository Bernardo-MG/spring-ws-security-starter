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

package com.bernardomg.security.user.data.adapter.inbound.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bernardomg.security.user.data.adapter.inbound.jpa.model.UserEntity;

/**
 * User repository.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public interface UserSpringRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Deletes the user with the received username.
     *
     * @param username
     *            username to delete
     */
    public void deleteByUsername(final String username);

    /**
     * Returns whether an user with the given email exists.
     *
     * @param email
     *            email to search for
     * @return {@code true} if a user exists, {@code false} otherwise
     */
    public boolean existsByEmail(final String email);

    /**
     * Returns whether an user with the given username exists.
     *
     * @param username
     *            username to search for
     * @return {@code true} if a user exists, {@code false} otherwise
     */
    public boolean existsByUsername(final String username);

    /**
     * Returns whether an user with the given email exists, ignoring a specific user.
     *
     * @param username
     *            username of the user to ignore
     * @param email
     *            email to search for
     * @return {@code true} if a user exists, {@code false} otherwise
     */
    public boolean existsByUsernameNotAndEmail(final String username, final String email);

    /**
     * Returns the user for the received email.
     *
     * @param email
     *            email to search for
     * @return the user for the received email
     */
    public Optional<UserEntity> findByEmail(final String email);

    /**
     * Returns the user for the received username.
     *
     * @param username
     *            username to search for
     * @return the user for the received username
     */
    public Optional<UserEntity> findByUsername(final String username);

}
