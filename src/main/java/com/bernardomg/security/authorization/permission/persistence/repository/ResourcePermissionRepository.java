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

package com.bernardomg.security.authorization.permission.persistence.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bernardomg.security.authorization.permission.persistence.model.ResourcePermissionEntity;

/**
 * Resource permission repository.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public interface ResourcePermissionRepository extends JpaRepository<ResourcePermissionEntity, Long> {

    /**
     * Returns all the permissions available to a role, in a paginated form.
     *
     * @param roleId
     *            role id
     * @param page
     *            pagination to apply
     * @return a page with the permissions
     */
    @Query("SELECT p FROM ResourcePermission p LEFT JOIN RolePermission rp ON p.name = rp.permission AND rp.roleId = :roleId WHERE rp.permission IS NULL OR rp.granted = false")
    public Page<ResourcePermissionEntity> findAllAvailableToRole(@Param("roleId") final Long roleId,
            final Pageable page);

    /**
     * Returns all the permissions assigned to a role, in a paginated form..
     *
     * @param roleId
     *            role id
     * @param page
     *            pagination to apply
     * @return a page with the permissions
     */
    @Query("SELECT p FROM ResourcePermission p INNER JOIN RolePermission rp ON p.name = rp.permission AND rp.roleId = :roleId WHERE rp.granted = true")
    public Page<ResourcePermissionEntity> findAllForRole(@Param("roleId") final Long roleId, final Pageable page);

    /**
     * Returns all the permissions available to a user.
     *
     * @param userId
     *            user id
     * @return a page with the permissions
     */
    @Query("SELECT p FROM ResourcePermission p INNER JOIN RolePermission rp ON p.name = rp.permission INNER JOIN Role r ON r.id = rp.roleId INNER JOIN UserRole ur ON ur.roleId = r.id INNER JOIN User u ON u.id = ur.userId WHERE u.id = :userId AND rp.granted = true")
    public Collection<ResourcePermissionEntity> findAllForUser(@Param("userId") final Long userId);

    /**
     * Returns all the permissions available to a user.
     *
     * @param username
     *            user username
     * @return a page with the permissions
     */
    @Query("SELECT p FROM ResourcePermission p INNER JOIN RolePermission rp ON p.name = rp.permission INNER JOIN Role r ON r.id = rp.roleId INNER JOIN UserRole ur ON ur.roleId = r.id INNER JOIN User u ON u.id = ur.userId WHERE u.username = :username AND rp.granted = true")
    public Collection<ResourcePermissionEntity> findAllForUser(@Param("username") final String username);

    public Optional<ResourcePermissionEntity> findByName(final String name);

}
