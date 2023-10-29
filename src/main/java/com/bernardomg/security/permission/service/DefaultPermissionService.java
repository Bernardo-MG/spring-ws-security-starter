
package com.bernardomg.security.permission.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.permission.model.DtoPermission;
import com.bernardomg.security.permission.model.Permission;
import com.bernardomg.security.permission.persistence.model.PersistentPermission;
import com.bernardomg.security.permission.persistence.repository.PermissionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DefaultPermissionService implements PermissionService {

    private final PermissionRepository repository;

    public DefaultPermissionService(final PermissionRepository repository) {
        super();

        this.repository = repository;
    }

    @Override
    public final Iterable<Permission> getAll(final Pageable pageable) {
        log.debug("Reading actions with pagination {}", pageable);

        return repository.findAll(pageable)
            .map(this::toDto);
    }

    @Override
    public final Optional<Permission> getOne(final long id) {

        log.debug("Reading action with id {}", id);

        return repository.findById(id)
            .map(this::toDto);
    }

    private final Permission toDto(final PersistentPermission entity) {
        return DtoPermission.builder()
            .id(entity.getId())
            .resource(entity.getResource())
            .action(entity.getAction())
            .build();
    }

}
