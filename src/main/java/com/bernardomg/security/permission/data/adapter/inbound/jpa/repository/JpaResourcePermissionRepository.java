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

package com.bernardomg.security.permission.data.adapter.inbound.jpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.data.springframework.SpringPagination;
import com.bernardomg.security.permission.data.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.permission.data.adapter.inbound.jpa.model.ResourcePermissionEntityMapper;
import com.bernardomg.security.permission.data.domain.model.ResourcePermission;
import com.bernardomg.security.permission.data.domain.repository.ResourcePermissionRepository;

/**
 * Resource permissions repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Transactional
public final class JpaResourcePermissionRepository implements ResourcePermissionRepository {

    /**
     * Logger for the class.
     */
    private static final Logger                      log = LoggerFactory
        .getLogger(JpaResourcePermissionRepository.class);

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
            .map(ResourcePermissionEntityMapper::toDomain)
            .distinct()
            .toList();
    }

    @Override
    public final Page<ResourcePermission> findAll(final Pagination pagination, final Sorting sorting) {
        final Pageable                                                 pageable;
        final org.springframework.data.domain.Page<ResourcePermission> page;

        log.debug("Finding all permissions for pagination {} and sorting {}", pagination, sorting);

        pageable = SpringPagination.toPageable(pagination, sorting);
        page = resourcePermissionSpringRepository.findAll(pageable)
            .map(ResourcePermissionEntityMapper::toDomain);

        return SpringPagination.toPage(page);
    }

    @Override
    public final Collection<String> findAllNames() {
        return resourcePermissionSpringRepository.findAllNames();
    }

    @Override
    public final Collection<ResourcePermission> save(final Collection<ResourcePermission> permissions) {
        final List<ResourcePermissionEntity> entities;
        final List<ResourcePermissionEntity> created;

        log.debug("Saving resource permissions {}", permissions);

        entities = permissions.stream()
            .map(ResourcePermissionEntityMapper::toEntity)
            .toList();
        entities.forEach(this::loadId);

        created = resourcePermissionSpringRepository.saveAll(entities);

        return created.stream()
            .map(ResourcePermissionEntityMapper::toDomain)
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

}
