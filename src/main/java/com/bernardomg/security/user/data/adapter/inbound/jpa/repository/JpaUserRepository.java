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

package com.bernardomg.security.user.data.adapter.inbound.jpa.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.data.springframework.SpringPagination;
import com.bernardomg.security.permission.data.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.permission.data.domain.comparator.ResourcePermissionComparator;
import com.bernardomg.security.permission.data.domain.model.ResourcePermission;
import com.bernardomg.security.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.role.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.role.adapter.inbound.jpa.repository.RoleSpringRepository;
import com.bernardomg.security.role.domain.comparator.RoleComparator;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.user.data.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.user.data.domain.model.User;
import com.bernardomg.security.user.data.domain.model.UserQuery;
import com.bernardomg.security.user.data.domain.repository.UserRepository;

/**
 * User repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Transactional
public final class JpaUserRepository implements UserRepository {

    /**
     * Logger for the class.
     */
    private static final Logger        log = LoggerFactory.getLogger(JpaUserRepository.class);

    /**
     * Password encoder, for validating passwords.
     */
    private final PasswordEncoder      passwordEncoder;

    /**
     * Role repository.
     */
    private final RoleSpringRepository roleSpringRepository;

    /**
     * User repository.
     */
    private final UserSpringRepository userSpringRepository;

    public JpaUserRepository(final UserSpringRepository userSpringRepo, final RoleSpringRepository roleSpringRepo,
            final PasswordEncoder passEncoder) {
        super();

        userSpringRepository = Objects.requireNonNull(userSpringRepo);
        roleSpringRepository = Objects.requireNonNull(roleSpringRepo);
        passwordEncoder = Objects.requireNonNull(passEncoder);
    }

    @Override
    public final User activate(final String username, final String password) {
        final Optional<UserEntity> read;
        final UserEntity           user;
        final UserEntity           updated;
        final User                 result;
        final String               encodedPassword;

        log.trace("Activating {}", username);

        read = userSpringRepository.findByUsername(username);
        if (read.isPresent()) {
            user = read.get();

            // Encode password
            encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);

            user.setEnabled(true);
            user.setPasswordNotExpired(true);
            updated = userSpringRepository.save(user);
            result = toDomain(updated);
        } else {
            // TODO: Maybe return an optional
            log.warn("User {} not found", username);
            result = new User(null, null, null, false, false, false, false, null);
        }

        return result;
    }

    @Override
    public final void clearLoginAttempts(final String username) {
        final Optional<UserEntity> existing;
        final UserEntity           toSave;

        log.trace("Clearing login attempts for {}", username);

        existing = userSpringRepository.findByUsername(username);
        if (existing.isPresent()) {
            toSave = existing.get();
            toSave.setLoginAttempts(0);

            userSpringRepository.save(toSave);
        } else {
            log.warn("User {} not found", username);
        }
    }

    @Override
    public final void delete(final String username) {
        log.trace("Deleting user {}", username);

        userSpringRepository.deleteByUsername(username);
    }

    @Override
    public final boolean exists(final String username) {
        log.trace("Checking if user {} exists", username);

        return userSpringRepository.existsByUsername(username);
    }

    @Override
    public final boolean existsByEmail(final String email) {
        log.trace("Checking if user exists by email {}", email);

        return userSpringRepository.existsByEmail(email);
    }

    @Override
    public final boolean existsEmailForAnotherUser(final String username, final String email) {
        log.trace("Checking if email {} is assigned to a user who is not {}", email, username);

        return userSpringRepository.existsByUsernameNotAndEmail(username, email);
    }

    @Override
    public final Page<User> findAll(final UserQuery query, final Pagination pagination, final Sorting sorting) {
        final UserEntity                                 entity;
        final Pageable                                   pageable;
        final org.springframework.data.domain.Page<User> page;

        log.trace("Finding users for query {} with pagination {} and sorting {}", query, pagination, sorting);

        entity = toEntity(query);
        pageable = SpringPagination.toPageable(pagination, sorting);
        page = userSpringRepository.findAll(Example.of(entity), pageable)
            .map(this::toDomain);

        return new Page<>(page.getContent(), page.getSize(), page.getNumber(), page.getTotalElements(),
            page.getTotalPages(), page.getNumberOfElements(), page.isFirst(), page.isLast(), sorting);
    }

    @Override
    public final int findLoginAttempts(final String username) {
        log.trace("Finding login attempts for user {}", username);

        return userSpringRepository.findByUsername(username)
            .map(UserEntity::getLoginAttempts)
            .orElse(0);
    }

    @Override
    public final Optional<User> findOne(final String username) {
        log.trace("Finding user {}", username);

        return userSpringRepository.findByUsername(username)
            .map(this::toDomain);
    }

    @Override
    public final Optional<User> findOneByEmail(final String email) {
        log.trace("Finding user by email {}", email);

        return userSpringRepository.findByEmail(email)
            .map(this::toDomain);
    }

    @Override
    public final Optional<String> findPassword(final String username) {
        log.trace("Finding password for user {}", username);

        return userSpringRepository.findByUsername(username)
            .map(UserEntity::getPassword);
    }

    @Override
    public final int increaseLoginAttempts(final String username) {
        final Optional<UserEntity> existing;
        final UserEntity           toSave;
        final int                  attempts;

        log.trace("Increasing login attempts for user {}", username);

        // TODO: maybe this should be done by the service
        existing = userSpringRepository.findByUsername(username);
        if (existing.isPresent()) {
            toSave = existing.get();
            attempts = toSave.getLoginAttempts() + 1;
            toSave.setLoginAttempts(attempts);

            userSpringRepository.save(toSave);
        } else {
            attempts = -1;
        }

        return attempts;
    }

    @Override
    public final User lock(final String username) {
        final Optional<UserEntity> read;
        final UserEntity           user;
        final UserEntity           updated;
        final User                 result;

        log.trace("Locking user {}", username);

        // TODO: maybe this should be done by the service
        read = userSpringRepository.findByUsername(username);
        if (read.isPresent()) {
            user = read.get();
            user.setNotLocked(false);
            updated = userSpringRepository.save(user);
            result = toDomain(updated);
        } else {
            // TODO: Maybe return an optional
            result = new User(null, null, null, false, false, false, false, null);
        }

        return result;
    }

    @Override
    public final User resetPassword(final String username, final String password) {
        final Optional<UserEntity> read;
        final UserEntity           user;
        final UserEntity           updated;
        final User                 result;
        final String               encodedPassword;

        log.trace("Resetting pasword for {}", username);

        // TODO: maybe this should be done by the service
        read = userSpringRepository.findByUsername(username);
        if (read.isPresent()) {
            user = read.get();

            // Encode password
            // TODO: shouldn't be encoded in the service?
            encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);

            user.setPasswordNotExpired(true);
            updated = userSpringRepository.save(user);
            result = toDomain(updated);
        } else {
            // TODO: Maybe return an optional
            result = new User(null, null, null, false, false, false, false, null);
        }

        return result;
    }

    @Override
    public final User save(final User user) {
        final Optional<UserEntity> existing;
        final UserEntity           entity;
        final UserEntity           saved;

        log.trace("Saving user");

        entity = toEntity(user);

        existing = userSpringRepository.findByUsername(user.username());
        if (existing.isPresent()) {
            entity.setId(existing.get()
                .getId());
            entity.setPassword(existing.get()
                .getPassword());
            entity.setLoginAttempts(existing.get()
                .getLoginAttempts());
        } else {
            entity.setPassword("");
        }

        saved = userSpringRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public final User saveNewUser(final User user) {
        final String     encodedPassword;
        final UserEntity entity;
        final UserEntity saved;

        log.trace("Saving new user");

        // TODO: seems to be a business usecase
        entity = toEntity(user);

        encodedPassword = passwordEncoder.encode("");
        entity.setPassword(encodedPassword);

        saved = userSpringRepository.save(entity);

        return toDomain(saved);
    }

    private final ResourcePermission toDomain(final ResourcePermissionEntity entity) {
        return new ResourcePermission(entity.getResource(), entity.getAction());
    }

    private final Role toDomain(final RoleEntity role) {
        final Collection<ResourcePermission> permissions;

        permissions = role.getPermissions()
            .stream()
            .filter(Objects::nonNull)
            .filter(RolePermissionEntity::getGranted)
            .map(RolePermissionEntity::getResourcePermission)
            .map(this::toDomain)
            .sorted(new ResourcePermissionComparator())
            .toList();
        return new Role(role.getName(), permissions);
    }

    private final User toDomain(final UserEntity user) {
        final Collection<Role> roles;

        roles = user.getRoles()
            .stream()
            .filter(Objects::nonNull)
            .map(this::toDomain)
            .sorted(new RoleComparator())
            .toList();
        return new User(user.getEmail(), user.getUsername(), user.getName(), user.getEnabled(), user.getNotExpired(),
            user.getNotLocked(), user.getPasswordNotExpired(), roles);
    }

    private final RoleEntity toEntity(final Role role) {
        final Optional<RoleEntity> read;

        read = roleSpringRepository.findByName(role.name());

        return read.orElse(null);
    }

    private final UserEntity toEntity(final User user) {
        final Collection<RoleEntity> roles;
        final UserEntity             entity;

        roles = user.roles()
            .stream()
            .map(this::toEntity)
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(ArrayList::new));

        entity = new UserEntity();
        entity.setUsername(user.username());
        entity.setName(user.name());
        entity.setEmail(user.email());
        entity.setEnabled(user.enabled());
        entity.setNotExpired(user.notExpired());
        entity.setNotLocked(user.notLocked());
        entity.setPasswordNotExpired(user.passwordNotExpired());
        entity.setRoles(roles);
        entity.setLoginAttempts(0);

        return entity;
    }

    private final UserEntity toEntity(final UserQuery user) {
        final UserEntity entity;

        entity = new UserEntity();
        entity.setUsername(user.username());
        entity.setName(user.name());
        entity.setEmail(user.email());
        entity.setEnabled(user.enabled());
        entity.setNotExpired(user.notExpired());
        entity.setNotLocked(user.notLocked());
        entity.setPasswordNotExpired(user.passwordNotExpired());

        return entity;
    }

}
