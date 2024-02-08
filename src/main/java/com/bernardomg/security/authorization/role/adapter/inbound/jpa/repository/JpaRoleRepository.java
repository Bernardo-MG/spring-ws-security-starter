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

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
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

    private final RoleSpringRepository roleRepository;

    private final UserSpringRepository userRepository;

    public JpaRoleRepository(final RoleSpringRepository roleRepo, final UserSpringRepository userRepo) {
        super();

        roleRepository = roleRepo;
        userRepository = userRepo;
    }

    @Override
    public final void delete(final String name) {
        roleRepository.deleteByName(name);
    }

    @Override
    public final boolean exists(final String name) {
        return roleRepository.existsByName(name);
    }

    @Override
    public final Iterable<Role> findAll(final RoleQuery query, final Pageable page) {
        final RoleEntity sample;

        sample = toEntity(query);

        return roleRepository.findAll(Example.of(sample), page)
            .map(this::toDomain);
    }

    @Override
    public final Iterable<Role> findAvailableToUser(final String username, final Pageable page) {
        final boolean        exists;
        final Iterable<Role> roles;

        exists = userRepository.existsByUsername(username);
        if (exists) {
            roles = roleRepository.findAvailableToUser(username, page)
                .map(this::toDomain);
        } else {
            roles = List.of();
        }

        return roles;
    }

    @Override
    public final Iterable<Role> findForUser(final String username, final Pageable page) {
        return roleRepository.findForUser(username, page)
            .map(this::toDomain);
    }

    @Override
    public final Optional<Role> findOne(final String name) {
        return roleRepository.findOneByName(name)
            .map(this::toDomain);
    }

    @Override
    public final Role save(final Role role) {
        final Optional<RoleEntity> existing;
        final RoleEntity           entity;
        final RoleEntity           saved;

        entity = toEntity(role);

        existing = roleRepository.findOneByName(role.getName());
        if (existing.isPresent()) {
            entity.setId(existing.get()
                .getId());
        }

        saved = roleRepository.save(entity);

        return toDomain(saved);
    }

    private final Role toDomain(final RoleEntity role) {
        return Role.builder()
            .withName(role.getName())
            .build();
    }

    private final RoleEntity toEntity(final Role role) {
        return RoleEntity.builder()
            .withName(role.getName())
            .build();
    }

    private final RoleEntity toEntity(final RoleQuery role) {
        return RoleEntity.builder()
            .withName(role.getName())
            .build();
    }

}
