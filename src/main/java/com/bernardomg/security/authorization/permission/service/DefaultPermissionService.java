
package com.bernardomg.security.authorization.permission.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.permission.model.ImmutableResourcePermission;
import com.bernardomg.security.authorization.permission.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.persistence.model.PersistentPermission;
import com.bernardomg.security.authorization.permission.persistence.repository.PermissionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DefaultPermissionService implements PermissionService {

    private final PermissionRepository repository;

    public DefaultPermissionService(final PermissionRepository repository) {
        super();

        this.repository = repository;
    }

    @Override
    public final Iterable<ResourcePermission> getAll(final Pageable pageable) {
        log.debug("Reading actions with pagination {}", pageable);

        return repository.findAll(pageable)
            .map(this::toDto);
    }

    @Override
    public final Optional<ResourcePermission> getOne(final long id) {

        log.debug("Reading action with id {}", id);

        return repository.findById(id)
            .map(this::toDto);
    }

    private final ResourcePermission toDto(final PersistentPermission entity) {
        return ImmutableResourcePermission.builder()
            .id(entity.getId())
            .resource(entity.getResource())
            .action(entity.getAction())
            .build();
    }

}
