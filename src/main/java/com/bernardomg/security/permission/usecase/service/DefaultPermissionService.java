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

package com.bernardomg.security.permission.usecase.service;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.permission.domain.model.ResourcePermission;
import com.bernardomg.security.permission.domain.repository.ResourcePermissionRepository;
import com.bernardomg.security.role.domain.repository.RolePermissionRepository;
import com.bernardomg.security.role.domain.repository.RoleRepository;

/**
 * Default role service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Transactional
public final class DefaultPermissionService implements PermissionService {

    /**
     * Logger for the class.
     */
    private static final Logger                log = LoggerFactory.getLogger(DefaultPermissionService.class);

    /**
     * Resource permission repository.
     */
    private final ResourcePermissionRepository resourcePermissionRepository;

    public DefaultPermissionService(final RoleRepository roleRepo, final RolePermissionRepository rolePermissionRepo,
            final ResourcePermissionRepository resourcePermissionRepo) {
        super();

        resourcePermissionRepository = Objects.requireNonNull(resourcePermissionRepo);
    }

    @Override
    public final Page<ResourcePermission> getAll(final Pagination pagination, final Sorting sorting) {
        final Page<ResourcePermission> roles;

        log.trace("Reading roles with pagination {} and sorting {}", pagination, sorting);

        roles = resourcePermissionRepository.findAll(pagination, sorting);

        log.trace("Read roles with pagination {} and sorting {}", pagination, sorting);

        return roles;
    }

}
