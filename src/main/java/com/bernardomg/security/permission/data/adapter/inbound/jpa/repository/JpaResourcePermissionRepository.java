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

package com.bernardomg.security.permission.data.adapter.inbound.jpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.permission.data.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.permission.data.domain.model.ResourcePermission;
import com.bernardomg.security.permission.data.domain.repository.ResourcePermissionRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Resource permissions repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Slf4j
@Transactional
public final class JpaResourcePermissionRepository implements ResourcePermissionRepository {

    /**
     * Resource permissions repository. Used not only to return the permissions, but also to validate they exist.
     */
    private final ResourcePermissionSpringRepository resourcePermissionSpringRepository;

    public JpaResourcePermissionRepository(final ResourcePermissionSpringRepository resourcePermissionSpringRepo) {
        super();

        resourcePermissionSpringRepository = Objects.requireNonNull(resourcePermissionSpringRepo);
    }

    @Override
    public final boolean exists(final String name) {
        log.debug("Checking if resource permission {} exists", name);

        return resourcePermissionSpringRepository.existsByName(name);
    }

    @Override
    public final Collection<ResourcePermission> findAll() {
        return resourcePermissionSpringRepository.findAll()
            .stream()
            .map(this::toDomain)
            .distinct()
            .toList();
    }

    @Override
    public final Collection<ResourcePermission> save(final Collection<ResourcePermission> permissions) {
        final List<ResourcePermissionEntity> entities;
        final List<ResourcePermissionEntity> created;

        log.debug("Saving resource permissions {}", permissions);

        entities = permissions.stream()
            .map(this::toEntity)
            .toList();
        entities.forEach(this::loadId);

        created = resourcePermissionSpringRepository.saveAll(entities);

        return created.stream()
            .map(this::toDomain)
            .toList();
    }

    private final void loadId(final ResourcePermissionEntity entity) {
        final Optional<ResourcePermissionEntity> existing;

        existing = resourcePermissionSpringRepository.findByName(entity.getName());
        if (existing.isPresent()) {
            entity.setId(existing.get()
                .getId());
        }
    }

    private final ResourcePermission toDomain(final ResourcePermissionEntity entity) {
        return new ResourcePermission(entity.getResource(), entity.getAction());
    }

    private final ResourcePermissionEntity toEntity(final ResourcePermission entity) {
        return ResourcePermissionEntity.builder()
            .withName(entity.getName())
            .withResource(entity.resource())
            .withAction(entity.action())
            .build();
    }

}
