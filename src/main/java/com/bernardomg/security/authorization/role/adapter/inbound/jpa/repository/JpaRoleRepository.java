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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository.ResourcePermissionSpringRepository;
import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RolePermissionId;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.UserRoleEntity;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.model.request.RoleQuery;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;

/**
 * Role repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Transactional
public final class JpaRoleRepository implements RoleRepository {

    private final ResourcePermissionSpringRepository resourcePermissionSpringRepository;

    private final RoleSpringRepository               roleSpringRepository;

    private final UserRoleSpringRepository           userRoleSpringRepository;

    public JpaRoleRepository(final RoleSpringRepository roleSpringRepo,
            final ResourcePermissionSpringRepository resourcePermissionSpringRepo,
            final UserRoleSpringRepository userRoleSpringRepo) {
        super();

        roleSpringRepository = roleSpringRepo;
        resourcePermissionSpringRepository = resourcePermissionSpringRepo;
        userRoleSpringRepository = userRoleSpringRepo;
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
    public final Optional<Role> findOne(final String name) {
        return roleSpringRepository.findOneByName(name)
            .map(this::toDomain);
    }

    @Override
    public final boolean isLinkedToUser(final String role) {
        final UserRoleEntity       sample;
        final Optional<RoleEntity> roleEntity;
        final boolean              exists;

        // TODO: rename, it is not clear what this method is for
        // TODO: the roles shouldn't know about users

        roleEntity = roleSpringRepository.findOneByName(role);
        if (roleEntity.isPresent()) {
            sample = UserRoleEntity.builder()
                .withRoleId(roleEntity.get()
                    .getId())
                .build();

            exists = userRoleSpringRepository.exists(Example.of(sample));
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

        entity = toEntity(role);

        existing = roleSpringRepository.findOneByName(role.getName());
        if (existing.isPresent()) {
            entity.setId(existing.get()
                .getId());
        }

        permissions = new ArrayList<>(entity.getPermissions());
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

    private final int comparePermission(final ResourcePermission left, final ResourcePermission right) {
        return left.getName()
            .compareTo(right.getName());
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
                .filter(Objects::nonNull)
                .filter(RolePermissionEntity::getGranted)
                .map(RolePermissionEntity::getResourcePermission)
                .map(this::toDomain)
                .filter(Objects::nonNull)
                .sorted(this::comparePermission)
                .toList();
        }
        return Role.builder()
            .withName(role.getName())
            .withPermissions(permissions)
            .build();
    }

    private final RolePermissionEntity toEntity(final ResourcePermission permission) {
        final Optional<ResourcePermissionEntity> read;
        final ResourcePermissionEntity           resourceEntity;
        final RolePermissionEntity               entity;
        final RolePermissionId                   id;

        read = resourcePermissionSpringRepository.findByName(permission.getName());

        if (read.isPresent()) {
            resourceEntity = read.get();
            id = RolePermissionId.builder()
                .withPermission(resourceEntity.getName())
                .build();
            entity = RolePermissionEntity.builder()
                .withGranted(true)
                .withId(id)
                .withResourcePermission(resourceEntity)
                .build();
        } else {
            entity = null;
        }

        return entity;
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
