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

package com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;

/**
 * Role repository.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public interface RoleSpringRepository extends JpaRepository<RoleEntity, Long> {

    /**
     * Deletes the role with the received name.
     *
     * @param name
     *            name of the role to delete
     */
    public void deleteByName(final String name);

    /**
     * Checks if a role with the received name exists.
     *
     * @param name
     *            role name
     * @return {@code true} if it exists, {@code false} otherwise
     */
    public boolean existsByNameIgnoreCase(final String name);

    /**
     * Returns all the roles available to the user, in a paginated form.
     *
     * @param username
     *            user username
     * @param page
     *            pagination to apply
     * @return a page with the roles
     */
    @Query("""
               SELECT r2
               FROM Role r2
               WHERE EXISTS (
                   SELECT 1
                   FROM User u
                   WHERE u.username = :username
               ) AND r2.id NOT IN (
                   SELECT r.id
                   FROM Role r
                   JOIN UserRole ur ON r.id = ur.roleId
                   JOIN User u ON ur.userId = u.id
                   WHERE u.username = :username
               )
            """)
    public Page<RoleEntity> findAllByUser(@Param("username") final String username, final Pageable page);

    /**
     * Returns the role for the received name.
     *
     * @param name
     *            name to search for
     * @return the role for the received name
     */
    public Optional<RoleEntity> findByName(final String name);

    /**
     * Returns all the roles assigned to the user, in a paginated form.
     *
     * @param username
     *            user username
     * @param page
     *            pagination to apply
     * @return a page with the roles
     */
    @Query("""
               SELECT r
               FROM Role r
                 JOIN UserRole ur ON r.id = ur.roleId
                 JOIN User u ON ur.userId = u.id
               WHERE u.username = :username
            """)
    public Page<RoleEntity> findByUser(@Param("username") final String username, final Pageable page);

}
