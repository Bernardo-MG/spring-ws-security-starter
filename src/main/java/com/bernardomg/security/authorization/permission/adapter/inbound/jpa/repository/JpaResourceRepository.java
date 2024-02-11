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

import java.util.Optional;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ResourceEntity;
import com.bernardomg.security.authorization.permission.domain.model.Resource;
import com.bernardomg.security.authorization.permission.domain.repository.ResourceRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Resource repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Slf4j
public final class JpaResourceRepository implements ResourceRepository {

    private final ResourceSpringRepository resourceSpringRepository;

    public JpaResourceRepository(final ResourceSpringRepository resourceSpringRepo) {
        super();

        resourceSpringRepository = resourceSpringRepo;
    }

    @Override
    public final boolean exists(final String name) {
        log.debug("Checking if resource {} exists", name);

        return resourceSpringRepository.existsByName(name);
    }

    @Override
    public final Resource save(final Resource resource) {
        final Optional<ResourceEntity> existing;
        final ResourceEntity           entity;
        final ResourceEntity           created;

        log.debug("Saving resource {}", resource);

        entity = toEntity(resource);

        existing = resourceSpringRepository.findByName(resource.getName());
        if (existing.isPresent()) {
            entity.setId(existing.get()
                .getId());
        }

        created = resourceSpringRepository.save(entity);

        return toDomain(created);
    }

    private final Resource toDomain(final ResourceEntity entity) {
        return Resource.builder()
            .withName(entity.getName())
            .build();
    }

    private final ResourceEntity toEntity(final Resource entity) {
        return ResourceEntity.builder()
            .withName(entity.getName())
            .build();
    }

}
