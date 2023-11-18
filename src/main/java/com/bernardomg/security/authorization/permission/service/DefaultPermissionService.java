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

package com.bernardomg.security.authorization.permission.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.model.ImmutableResourcePermission;
import com.bernardomg.security.authorization.permission.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.persistence.model.ResourcePermissionEntity;
import com.bernardomg.security.authorization.permission.persistence.repository.ResourcePermissionRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Default permissions service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class DefaultPermissionService implements PermissionService {

    /**
     * Permissions repository.
     */
    private final ResourcePermissionRepository resourcePermissionRepository;

    public DefaultPermissionService(final ResourcePermissionRepository resourcePermissionRepo) {
        super();

        resourcePermissionRepository = resourcePermissionRepo;
    }

    @Override
    public final Iterable<ResourcePermission> getAll(final Pageable pageable) {
        log.debug("Reading actions with pagination {}", pageable);

        return resourcePermissionRepository.findAll(pageable)
            .map(this::toDto);
    }

    @Override
    public final Optional<ResourcePermission> getOne(final long id) {

        log.debug("Reading action with id {}", id);

        return resourcePermissionRepository.findById(id)
            .map(this::toDto);
    }

    private final ResourcePermission toDto(final ResourcePermissionEntity entity) {
        return ImmutableResourcePermission.builder()
            .id(entity.getId())
            .resource(entity.getResource())
            .action(entity.getAction())
            .build();
    }

}
