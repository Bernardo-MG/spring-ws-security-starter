
package com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository;

import java.util.Optional;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ResourceEntity;
import com.bernardomg.security.authorization.permission.domain.model.Resource;
import com.bernardomg.security.authorization.permission.domain.repository.ResourceRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JpaResourceRepository implements ResourceRepository {

    private final ResourceSpringRepository resourceRepository;

    public JpaResourceRepository(final ResourceSpringRepository resourceRepo) {
        super();

        resourceRepository = resourceRepo;
    }

    @Override
    public final boolean exists(final String name) {
        return resourceRepository.existsByName(name);
    }

    @Override
    public final Resource save(final Resource resource) {
        final Optional<ResourceEntity> existing;
        final ResourceEntity           entity;
        final ResourceEntity           created;

        log.debug("Saving resource {}", resource);

        entity = toEntity(resource);

        existing = resourceRepository.findByName(resource.getName());
        if (existing.isPresent()) {
            entity.setId(existing.get()
                .getId());
        }

        created = resourceRepository.save(entity);

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
