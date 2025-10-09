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

package com.bernardomg.security.user.adapter.inbound.jpa.repository;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.data.springframework.SpringPagination;
import com.bernardomg.security.role.adapter.inbound.jpa.model.RoleEntityMapper;
import com.bernardomg.security.role.adapter.inbound.jpa.repository.RoleSpringRepository;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.user.domain.repository.UserRoleRepository;

/**
 * Role repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Transactional
public final class JpaUserRoleRepository implements UserRoleRepository {

    /**
     * Logger for the class.
     */
    private static final Logger        log = LoggerFactory.getLogger(JpaUserRoleRepository.class);

    /**
     * Role repository.
     */
    private final RoleSpringRepository roleSpringRepository;

    public JpaUserRoleRepository(final RoleSpringRepository roleSpringRepo) {
        super();

        roleSpringRepository = Objects.requireNonNull(roleSpringRepo);
    }

    @Override
    public final Page<Role> findAvailableToUser(final String username, final Pagination pagination,
            final Sorting sorting) {
        final Pageable                                   pageable;
        final org.springframework.data.domain.Page<Role> page;

        log.trace("Finding roles available to user {}", username);

        // TODO: this doesn't need the full role model, just the names
        pageable = SpringPagination.toPageable(pagination, sorting);
        page = roleSpringRepository.findAllByUser(username, pageable)
            .map(RoleEntityMapper::toDomain);

        return SpringPagination.toPage(page);
    }

}
