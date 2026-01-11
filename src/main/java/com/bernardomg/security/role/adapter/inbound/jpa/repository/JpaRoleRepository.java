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
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.data.springframework.SpringPagination;
import com.bernardomg.security.permission.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.permission.adapter.inbound.jpa.repository.ResourcePermissionSpringRepository;
import com.bernardomg.security.permission.domain.model.ResourcePermission;
import com.bernardomg.security.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.role.adapter.inbound.jpa.model.RoleEntityMapper;
import com.bernardomg.security.role.adapter.inbound.jpa.model.RolePermissionEntity;
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
    private static final Logger                        log = LoggerFactory.getLogger(JpaRoleRepository.class);

    /**
     * Resource permission repository.
     */
    private final ResourcePermissionSpringRepository   resourcePermissionSpringRepository;

    /**
     * Role permission repository.
     */
    private final RolePermissionEntitySpringRepository rolePermissionEntitySpringRepository;

    /**
     * Role repository.
     */
    private final RoleSpringRepository                 roleSpringRepository;

    /**
     * User roles repository.
     */
    private final UserRoleSpringRepository             userRoleSpringRepository;

    public JpaRoleRepository(final RoleSpringRepository roleSpringRepo,
            final ResourcePermissionSpringRepository resourcePermissionSpringRepo,
            final UserRoleSpringRepository userRoleSpringRepo,
            final RolePermissionEntitySpringRepository rolePermissionEntitySpringRepo) {
        super();

        roleSpringRepository = Objects.requireNonNull(roleSpringRepo);
        resourcePermissionSpringRepository = Objects.requireNonNull(resourcePermissionSpringRepo);
        userRoleSpringRepository = Objects.requireNonNull(userRoleSpringRepo);
        rolePermissionEntitySpringRepository = Objects.requireNonNull(rolePermissionEntitySpringRepo);
    }

    @Override
    public final void delete(final String name) {
        log.trace("Deleting role {}", name);

        roleSpringRepository.deleteByName(name);

        log.trace("Deleted role {}", name);
    }

    @Override
    public final boolean exists(final String name) {
        final boolean existing;

        log.trace("Checking role {} exists", name);

        existing = roleSpringRepository.existsByNameIgnoreCase(name);

        log.trace("Checked role {} exists: {}", name, existing);

        return existing;
    }

    @Override
    public final Page<Role> findAll(final RoleQuery query, final Pagination pagination, final Sorting sorting) {
        final RoleEntity                                 sample;
        final Pageable                                   pageable;
        final org.springframework.data.domain.Page<Role> page;
        final Page<Role>                                 read;

        log.debug("Finding all roles for query {} with pagination {} and sorting {}", query, pagination, sorting);

        sample = RoleEntityMapper.toEntity(query);

        pageable = SpringPagination.toPageable(pagination, sorting);
        page = roleSpringRepository.findAll(Example.of(sample), pageable)
            .map(RoleEntityMapper::toDomain);

        read = SpringPagination.toPage(page);

        log.debug("Found all roles for query {} with pagination {} and sorting {}: {}", query, pagination, sorting,
            read);

        return read;
    }

    @Override
    public final Optional<Role> findOne(final String name) {
        final Optional<Role> read;

        log.trace("Finding role {}", name);

        read = roleSpringRepository.findByName(name)
            .map(RoleEntityMapper::toDomain);

        log.trace("Found role {}", read);

        return read;
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

        log.trace("Checked if role {} is linked to user: {}", name, exists);

        return exists;
    }

    @Override
    public final Role save(final Role role) {
        final Optional<RoleEntity>             existing;
        final RoleEntity                       entity;
        final RoleEntity                       savedAgain;
        final Collection<RolePermissionEntity> permissions;
        final Collection<RolePermissionEntity> disabledPermissions;
        final Collection<RolePermissionEntity> newPermissions;
        final Role                             created;

        log.trace("Saving role {}", role);

        entity = toEntity(role);

        if (entity.getPermissions() == null) {
            permissions = new ArrayList<>();
        } else {
            permissions = new ArrayList<>(entity.getPermissions()
                .stream()
                .filter(Objects::nonNull)
                .toList());
        }

        existing = roleSpringRepository.findByName(role.name());
        if (existing.isPresent()) {
            entity.setId(existing.get()
                .getId());
            disabledPermissions = disablePermissions(permissions, existing.get()
                .getPermissions());
        } else {
            disabledPermissions = List.of();
        }

        // Disable existing permissions, and set role id
        newPermissions = Stream.concat(permissions.stream(), disabledPermissions.stream())
            .collect(Collectors.toCollection(ArrayList::new));
        newPermissions.forEach(p -> {
            p.setRoleId(entity.getId());
        });

        entity.setPermissions(newPermissions);
        savedAgain = roleSpringRepository.save(entity);

        rolePermissionEntitySpringRepository.saveAll(newPermissions);

        created = RoleEntityMapper.toDomain(savedAgain);

        log.trace("Saved role {}", created);

        return created;

    }

    private final Collection<RolePermissionEntity> disablePermissions(
            final Collection<RolePermissionEntity> newPermissions,
            final Collection<RolePermissionEntity> existingPermissions) {
        final Collection<String>               permissionNames;
        final Collection<RolePermissionEntity> disabledPermissions;
        permissionNames = newPermissions.stream()
            .map(p -> slug(p.getResourcePermission()))
            .distinct()
            .toList();
        disabledPermissions = existingPermissions.stream()
            .filter(p -> !permissionNames.contains(slug(p.getResourcePermission())))
            .toList();
        disabledPermissions.forEach(p -> p.setGranted(false));
        return disabledPermissions;
    }

    private final String slug(final ResourcePermissionEntity permission) {
        return permission.getResource() + "-" + permission.getAction();
    }

    private final RolePermissionEntity toEntity(final ResourcePermission permission) {
        final Optional<ResourcePermissionEntity> read;
        final ResourcePermissionEntity           resourceEntity;
        final RolePermissionEntity               entity;

        // TODO: move to mapper

        read = resourcePermissionSpringRepository.findByResourceAndAction(permission.resource(), permission.action());

        if (read.isPresent()) {
            resourceEntity = read.get();
            entity = new RolePermissionEntity();
            entity.setPermissionId(resourceEntity.getId());
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

        // TODO: move to mapper

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

}
