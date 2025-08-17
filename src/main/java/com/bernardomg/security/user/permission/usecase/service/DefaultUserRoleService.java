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

package com.bernardomg.security.user.permission.usecase.service;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.user.data.domain.exception.MissingUsernameException;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.permission.domain.repository.UserRoleRepository;

/**
 * Default user role service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Transactional
public final class DefaultUserRoleService implements UserRoleService {

    /**
     * Logger for the class.
     */
    private static final Logger      log = LoggerFactory.getLogger(DefaultUserRoleService.class);

    /**
     * User repository.
     */
    private final UserRepository     userRepository;

    /**
     * User role repository.
     */
    private final UserRoleRepository userRoleRepository;

    public DefaultUserRoleService(final UserRepository userRepo, final UserRoleRepository userRoleRepo) {
        super();

        userRepository = Objects.requireNonNull(userRepo);
        userRoleRepository = Objects.requireNonNull(userRoleRepo);
    }

    @Override
    public final Page<Role> getAvailableRoles(final String username, final Pagination pagination,
            final Sorting sorting) {
        final Page<Role> roles;

        log.trace("Reading available roles for {}", username);

        if (!userRepository.exists(username)) {
            log.error("Missing user {}", username);
            throw new MissingUsernameException(username);
        }

        roles = userRoleRepository.findAvailableToUser(username, pagination, sorting);

        log.trace("Read available roles for {}", username);

        return roles;
    }

}
