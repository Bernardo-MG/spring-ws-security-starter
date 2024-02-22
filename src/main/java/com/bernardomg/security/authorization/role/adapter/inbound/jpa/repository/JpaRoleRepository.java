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

package com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository.ResourcePermissionSpringRepository;
import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.model.request.RoleQuery;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;

/**
 * Role repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
public final class JpaRoleRepository implements RoleRepository {

    private final ResourcePermissionSpringRepository resourcePermissionSpringRepository;

    private final RoleSpringRepository               roleSpringRepository;

    private final UserSpringRepository               userSpringRepository;

    public JpaRoleRepository(final RoleSpringRepository roleSpringRepo, final UserSpringRepository userSpringRepo,
            final ResourcePermissionSpringRepository resourcePermissionSpringRepo) {
        super();

        roleSpringRepository = roleSpringRepo;
        userSpringRepository = userSpringRepo;
        resourcePermissionSpringRepository = resourcePermissionSpringRepo;
    }

    @Override
    public final void delete(final String name) {
        roleSpringRepository.deleteByName(name);
    }

    @Override
    public final boolean exists(final String name) {
        return roleSpringRepository.existsByName(name);
    }

    @Override
    public final Iterable<Role> findAll(final RoleQuery query, final Pageable page) {
        final RoleEntity sample;

        sample = toEntity(query);

        return roleSpringRepository.findAll(Example.of(sample), page)
            .map(this::toDomain);
    }

    @Override
    public final Iterable<Role> findAvailableToUser(final String username, final Pageable page) {
        final boolean        exists;
        final Iterable<Role> roles;

        exists = userSpringRepository.existsByUsername(username);
        if (exists) {
            roles = roleSpringRepository.findAvailableToUser(username, page)
                .map(this::toDomain);
        } else {
            roles = List.of();
        }

        return roles;
    }

    @Override
    public final Iterable<Role> findForUser(final String username, final Pageable page) {
        return roleSpringRepository.findForUser(username, page)
            .map(this::toDomain);
    }

    @Override
    public final Optional<Role> findOne(final String name) {
        return roleSpringRepository.findOneByName(name)
            .map(this::toDomain);
    }

    @Override
    public final Role save(final Role role) {
        final Optional<RoleEntity> existing;
        final RoleEntity           entity;
        final RoleEntity           saved;

        entity = toEntity(role);

        existing = roleSpringRepository.findOneByName(role.getName());
        if (existing.isPresent()) {
            entity.setId(existing.get()
                .getId());
        }

        saved = roleSpringRepository.save(entity);

        return toDomain(saved);
    }

    private final ResourcePermission toDomain(final ResourcePermissionEntity entity) {
        return ResourcePermission.builder()
            .withName(entity.getName())
            .withResource(entity.getResource())
            .withAction(entity.getAction())
            .build();
    }

    private final Role toDomain(final RoleEntity role) {
        final Collection<ResourcePermission> permissions;

        if (role.getPermissions() == null) {
            permissions = List.of();
        } else {
            permissions = role.getPermissions()
                .stream()
                .filter(RolePermissionEntity::getGranted)
                .map(RolePermissionEntity::getResourcePermission)
                .map(this::toDomain)
                .toList();
        }
        return Role.builder()
            .withName(role.getName())
            .withPermissions(permissions)
            .build();
    }

    private final RolePermissionEntity toEntity(final ResourcePermission permission) {
        final ResourcePermissionEntity resourceEntity;

        // TODO: check if it exists
        resourceEntity = resourcePermissionSpringRepository.findByName(permission.getName())
            .get();

        return RolePermissionEntity.builder()
            .withGranted(true)
            .withPermission(resourceEntity.getName())
            .withResourcePermission(resourceEntity)
            .build();
    }

    private final RoleEntity toEntity(final Role role) {
        final Collection<RolePermissionEntity> permissions;

        permissions = role.getPermissions()
            .stream()
            .map(this::toEntity)
            .toList();
        return RoleEntity.builder()
            .withName(role.getName())
            .withPermissions(permissions)
            .build();
    }

    private final RoleEntity toEntity(final RoleQuery role) {
        return RoleEntity.builder()
            .withName(role.getName())
            .build();
    }

}
