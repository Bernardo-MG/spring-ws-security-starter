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

package com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.domain.repository.ResourcePermissionRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Resource permissions repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Slf4j
public final class JpaResourcePermissionRepository implements ResourcePermissionRepository {

    /**
     * Resource permissions repository. Used not only to return the permissions, but also to validate they exist.
     */
    private final ResourcePermissionSpringRepository resourcePermissionRepository;

    private final UserSpringRepository               userRepository;

    public JpaResourcePermissionRepository(final UserSpringRepository userRepo,
            final ResourcePermissionSpringRepository resourcePermissionRepo) {
        super();

        userRepository = userRepo;
        resourcePermissionRepository = resourcePermissionRepo;
    }

    @Override
    public final boolean exists(final String name) {
        return resourcePermissionRepository.existsByName(name);
    }

    @Override
    public final Collection<ResourcePermission> findAll() {
        return resourcePermissionRepository.findAll()
            .stream()
            .map(this::toDomain)
            .distinct()
            .toList();
    }

    @Override
    public final Collection<ResourcePermission> findAllForUser(final String username) {
        final Optional<UserEntity>           user;
        final Collection<ResourcePermission> permissions;

        user = userRepository.findOneByUsername(username);
        if (user.isPresent()) {
            permissions = resourcePermissionRepository.findAllForUser(user.get()
                .getId())
                .stream()
                .map(this::toDomain)
                .distinct()
                .toList();
        } else {
            permissions = List.of();
        }

        return permissions;
    }

    @Override
    public final ResourcePermission save(final ResourcePermission permission) {
        final Optional<ResourcePermissionEntity> existing;
        final ResourcePermissionEntity           entity;
        final ResourcePermissionEntity           created;

        log.debug("Saving permission {}", permission);

        entity = toEntity(permission);

        existing = resourcePermissionRepository.findByName(permission.getName());
        if (existing.isPresent()) {
            entity.setId(existing.get()
                .getId());
        }

        created = resourcePermissionRepository.save(entity);

        return toDomain(created);
    }

    private final ResourcePermission toDomain(final ResourcePermissionEntity entity) {
        return ResourcePermission.builder()
            .withName(entity.getName())
            .withResource(entity.getResource())
            .withAction(entity.getAction())
            .build();
    }

    private final ResourcePermissionEntity toEntity(final ResourcePermission entity) {
        return ResourcePermissionEntity.builder()
            .withName(entity.getName())
            .withResource(entity.getResource())
            .withAction(entity.getAction())
            .build();
    }

}
