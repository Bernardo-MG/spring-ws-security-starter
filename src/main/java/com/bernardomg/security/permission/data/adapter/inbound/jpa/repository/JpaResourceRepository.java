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
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.permission.data.adapter.inbound.jpa.model.ResourceEntity;
import com.bernardomg.security.permission.data.adapter.inbound.jpa.model.ResourceEntityMapper;
import com.bernardomg.security.permission.data.domain.model.Resource;
import com.bernardomg.security.permission.data.domain.repository.ResourceRepository;

/**
 * Resource repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Transactional
public final class JpaResourceRepository implements ResourceRepository {

    /**
     * Logger for the class.
     */
    private static final Logger            log = LoggerFactory.getLogger(JpaResourceRepository.class);

    /**
     * Resource repository.
     */
    private final ResourceSpringRepository resourceSpringRepository;

    public JpaResourceRepository(final ResourceSpringRepository resourceSpringRepo) {
        super();

        resourceSpringRepository = Objects.requireNonNull(resourceSpringRepo);
    }

    @Override
    public final Collection<String> findAllNames() {
        return resourceSpringRepository.findAllNames();
    }

    @Override
    public final Collection<Resource> save(final Collection<Resource> resources) {
        final List<ResourceEntity> entities;
        final List<ResourceEntity> created;

        log.debug("Saving resources {}", resources);

        entities = resources.stream()
            .map(ResourceEntityMapper::toEntity)
            .toList();
        entities.forEach(this::loadId);

        created = resourceSpringRepository.saveAll(entities);

        return created.stream()
            .map(ResourceEntityMapper::toDomain)
            .toList();
    }

    private final void loadId(final ResourceEntity entity) {
        final Optional<ResourceEntity> existing;

        existing = resourceSpringRepository.findByName(entity.getName());
        if (existing.isPresent()) {
            entity.setId(existing.get()
                .getId());
        }
    }

}
