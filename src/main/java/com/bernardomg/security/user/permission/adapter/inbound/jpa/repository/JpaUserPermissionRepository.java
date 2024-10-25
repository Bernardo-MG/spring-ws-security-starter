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

package com.bernardomg.security.user.permission.adapter.inbound.jpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.permission.data.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.permission.data.adapter.inbound.jpa.repository.ResourcePermissionSpringRepository;
import com.bernardomg.security.permission.data.domain.model.ResourcePermission;
import com.bernardomg.security.user.data.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.user.data.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.user.permission.domain.repository.UserPermissionRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Resource permissions repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Slf4j
@Transactional
public final class JpaUserPermissionRepository implements UserPermissionRepository {

    /**
     * Resource permissions repository. Used not only to return the permissions, but also to validate they exist.
     */
    private final ResourcePermissionSpringRepository resourcePermissionRepository;

    /**
     * User repository.
     */
    private final UserSpringRepository               userRepository;

    public JpaUserPermissionRepository(final UserSpringRepository userSpringRepo,
            final ResourcePermissionSpringRepository resourcePermissionSpringRepo) {
        super();

        userRepository = Objects.requireNonNull(userSpringRepo);
        resourcePermissionRepository = Objects.requireNonNull(resourcePermissionSpringRepo);
    }

    @Override
    public final Collection<ResourcePermission> findAll(final String username) {
        final Optional<UserEntity>           user;
        final Collection<ResourcePermission> permissions;

        user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            permissions = resourcePermissionRepository.findAllForUser(user.get()
                .getId())
                .stream()
                .map(this::toDomain)
                .distinct()
                .toList();
        } else {
            log.warn("User {} doesn't exist. Cant find its permissions");
            permissions = List.of();
        }

        return permissions;
    }

    private final ResourcePermission toDomain(final ResourcePermissionEntity entity) {
        return new ResourcePermission(entity.getResource(), entity.getAction());
    }

}
