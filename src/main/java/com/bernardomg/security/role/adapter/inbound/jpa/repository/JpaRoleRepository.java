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

package com.bernardomg.security.role.adapter.inbound.jpa.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.data.springframework.SpringPagination;
import com.bernardomg.security.account.adapter.inbound.repository.JpaAccountRepository;
import com.bernardomg.security.permission.data.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.permission.data.adapter.inbound.jpa.repository.ResourcePermissionSpringRepository;
import com.bernardomg.security.permission.data.domain.comparator.ResourcePermissionComparator;
import com.bernardomg.security.permission.data.domain.model.ResourcePermission;
import com.bernardomg.security.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.role.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.role.adapter.inbound.jpa.model.RolePermissionId;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.domain.model.RoleQuery;
import com.bernardomg.security.role.domain.repository.RoleRepository;

/**
 * Role repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Transactional
public final class JpaRoleRepository implements RoleRepository {

    /**
     * Logger for the class.
     */
    private static final Logger                      log = LoggerFactory.getLogger(JpaAccountRepository.class);

    /**
     * Resource permission repository.
     */
    private final ResourcePermissionSpringRepository resourcePermissionSpringRepository;

    /**
     * Role repository.
     */
    private final RoleSpringRepository               roleSpringRepository;

    /**
     * User roles repository.
     */
    private final UserRoleSpringRepository           userRoleSpringRepository;

    public JpaRoleRepository(final RoleSpringRepository roleSpringRepo,
            final ResourcePermissionSpringRepository resourcePermissionSpringRepo,
            final UserRoleSpringRepository userRoleSpringRepo) {
        super();

        roleSpringRepository = Objects.requireNonNull(roleSpringRepo);
        resourcePermissionSpringRepository = Objects.requireNonNull(resourcePermissionSpringRepo);
        userRoleSpringRepository = Objects.requireNonNull(userRoleSpringRepo);
    }

    @Override
    public final void delete(final String name) {
        log.trace("Deleting role {}", name);

        roleSpringRepository.deleteByName(name);
    }

    @Override
    public final boolean exists(final String name) {
        log.trace("Checking role {} exists", name);

        return roleSpringRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public final Page<Role> findAll(final RoleQuery query, final Pagination pagination, final Sorting sorting) {
        final RoleEntity                                 sample;
        final Pageable                                   pageable;
        final org.springframework.data.domain.Page<Role> page;

        log.debug("Finding available permissions for query {} with pagination {} and sorting {}", query, pagination,
            sorting);

        sample = toEntity(query);

        pageable = SpringPagination.toPageable(pagination, sorting);
        page = roleSpringRepository.findAll(Example.of(sample), pageable)
            .map(this::toDomain);

        return new Page<>(page.getContent(), page.getSize(), page.getNumber(), page.getTotalElements(),
            page.getTotalPages(), page.getNumberOfElements(), page.isFirst(), page.isLast(), sorting);
    }

    @Override
    public final Optional<Role> findOne(final String name) {
        log.trace("Finding role {}", name);

        return roleSpringRepository.findByName(name)
            .map(this::toDomain);
    }

    @Override
    public final boolean isLinkedToUser(final String name) {
        final Optional<RoleEntity> roleEntity;
        final boolean              exists;

        log.trace("Checking if role {} is linked to user", name);

        // TODO: rename, it is not clear what this method is for
        // TODO: the roles shouldn't know about users

        roleEntity = roleSpringRepository.findByName(name);
        if (roleEntity.isPresent()) {
            exists = userRoleSpringRepository.existsByRoleId(roleEntity.get()
                .getId());
        } else {
            exists = false;
        }

        return exists;
    }

    @Override
    public final Role save(final Role role) {
        final Optional<RoleEntity>             existing;
        final RoleEntity                       entity;
        final RoleEntity                       saved;
        final RoleEntity                       savedAgain;
        final Collection<RolePermissionEntity> permissions;

        log.trace("Saving role {}", role);

        entity = toEntity(role);

        existing = roleSpringRepository.findByName(role.name());
        if (existing.isPresent()) {
            entity.setId(existing.get()
                .getId());
        }

        if (entity.getPermissions() == null) {
            permissions = new ArrayList<>();
        } else {
            permissions = new ArrayList<>(entity.getPermissions()
                .stream()
                .filter(Objects::nonNull)
                .toList());
        }
        entity.setPermissions(new ArrayList<>());
        saved = roleSpringRepository.save(entity);

        permissions.forEach(p -> {
            p.getId()
                .setRoleId(saved.getId());
        });
        saved.setPermissions(permissions);
        savedAgain = roleSpringRepository.save(saved);

        return toDomain(savedAgain);
    }

    private final ResourcePermission toDomain(final ResourcePermissionEntity entity) {
        return new ResourcePermission(entity.getResource(), entity.getAction());
    }

    private final Role toDomain(final RoleEntity role) {
        final Collection<ResourcePermission> permissions;

        if (role.getPermissions() == null) {
            permissions = List.of();
        } else {
            permissions = role.getPermissions()
                .stream()
                .filter(Objects::nonNull)
                .filter(RolePermissionEntity::getGranted)
                .map(RolePermissionEntity::getResourcePermission)
                .map(this::toDomain)
                .sorted(new ResourcePermissionComparator())
                .toList();
        }

        return new Role(role.getName(), permissions);
    }

    private final RolePermissionEntity toEntity(final ResourcePermission permission) {
        final Optional<ResourcePermissionEntity> read;
        final ResourcePermissionEntity           resourceEntity;
        final RolePermissionEntity               entity;
        final RolePermissionId                   id;

        read = resourcePermissionSpringRepository.findByName(permission.getName());

        if (read.isPresent()) {
            resourceEntity = read.get();
            id = new RolePermissionId();
            id.setPermission(resourceEntity.getName());
            entity = new RolePermissionEntity();
            entity.setId(id);
            entity.setGranted(true);
            entity.setResourcePermission(resourceEntity);
        } else {
            entity = null;
        }

        return entity;
    }

    private final RoleEntity toEntity(final Role role) {
        final Collection<RolePermissionEntity> permissions;
        final RoleEntity                       entity;

        permissions = role.permissions()
            .stream()
            .map(this::toEntity)
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(ArrayList::new));
        entity = new RoleEntity();
        entity.setName(role.name());
        entity.setPermissions(permissions);

        return entity;
    }

    private final RoleEntity toEntity(final RoleQuery role) {
        final RoleEntity entity;

        // TODO: does it make sense filtering by name?
        entity = new RoleEntity();
        entity.setName(role.name());

        return entity;
    }

}
