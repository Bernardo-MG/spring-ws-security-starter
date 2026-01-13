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

package com.bernardomg.security.user.adapter.inbound.jpa.repository;

import java.util.ArrayList;
import java.util.Collection;
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
import com.bernardomg.security.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.role.adapter.inbound.jpa.repository.RoleSpringRepository;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.user.adapter.inbound.jpa.model.UserEntityMapper;
import com.bernardomg.security.user.domain.model.User;
import com.bernardomg.security.user.domain.model.UserQuery;
import com.bernardomg.security.user.domain.repository.UserRepository;

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
     * Role repository.
     */
    private final RoleSpringRepository roleSpringRepository;

    /**
     * User repository.
     */
    private final UserSpringRepository userSpringRepository;

    public JpaUserRepository(final UserSpringRepository userSpringRepo, final RoleSpringRepository roleSpringRepo) {
        super();

        userSpringRepository = Objects.requireNonNull(userSpringRepo);
        roleSpringRepository = Objects.requireNonNull(roleSpringRepo);
    }

    @Override
    public final User activate(final String username, final String password) {
        final Optional<UserEntity> read;
        final UserEntity           user;
        final UserEntity           updated;
        final User                 result;

        log.trace("Activating {}", username);

        read = userSpringRepository.findByUsername(username);
        if (read.isPresent()) {
            user = read.get();
            user.setPassword(password);

            user.setEnabled(true);
            user.setPasswordNotExpired(true);
            updated = userSpringRepository.save(user);
            result = UserEntityMapper.toDomain(updated);

            log.trace("Activated {}", result);
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

            log.trace("Cleared login attempts for {}", username);
        } else {
            log.warn("User {} not found", username);
        }
    }

    @Override
    public final void delete(final String username) {
        log.trace("Deleting user {}", username);

        userSpringRepository.deleteByUsername(username);

        log.trace("Deleted user {}", username);
    }

    @Override
    public final boolean exists(final String username) {
        final boolean exists;

        log.trace("Checking if user {} exists", username);

        exists = userSpringRepository.existsByUsername(username);

        log.trace("Checked if user {} exists: {}", username, exists);

        return exists;
    }

    @Override
    public final boolean existsByEmail(final String email) {
        final boolean exists;

        log.trace("Checking if user exists by email {}", email);

        exists = userSpringRepository.existsByEmail(email);

        log.trace("Checked if user exists by email {}: {}", email, exists);

        return exists;
    }

    @Override
    public final boolean existsEmailForAnotherUser(final String username, final String email) {
        final boolean exists;

        log.trace("Checking if email {} is assigned to a user who is not {}", email, username);

        exists = userSpringRepository.existsByUsernameNotAndEmail(username, email);

        log.trace("Checked if email {} is assigned to a user who is not {}: {}", email, username, exists);

        return exists;
    }

    @Override
    public final Page<User> findAll(final UserQuery query, final Pagination pagination, final Sorting sorting) {
        final UserEntity                                 entity;
        final Pageable                                   pageable;
        final org.springframework.data.domain.Page<User> page;
        final Page<User>                                 read;

        log.trace("Finding users for query {} with pagination {} and sorting {}", query, pagination, sorting);

        entity = UserEntityMapper.toEntity(query);
        pageable = SpringPagination.toPageable(pagination, sorting);
        page = userSpringRepository.findAll(Example.of(entity), pageable)
            .map(UserEntityMapper::toDomain);

        read = SpringPagination.toPage(page);

        log.trace("Found users for query {} with pagination {} and sorting {}: {}", query, pagination, sorting, read);

        return read;
    }

    @Override
    public final int findLoginAttempts(final String username) {
        final int attempts;

        log.trace("Finding login attempts for user {}", username);

        attempts = userSpringRepository.findByUsername(username)
            .map(UserEntity::getLoginAttempts)
            .orElse(0);

        log.trace("Found login attempts for user {}: {}", username, attempts);

        return attempts;
    }

    @Override
    public final Optional<User> findOne(final String username) {
        final Optional<User> read;

        log.trace("Finding user {}", username);

        read = userSpringRepository.findByUsername(username)
            .map(UserEntityMapper::toDomain);

        log.trace("Found user {}", read);

        return read;
    }

    @Override
    public final Optional<User> findOneByEmail(final String email) {
        final Optional<User> read;

        log.trace("Finding user by email {}", email);

        read = userSpringRepository.findByEmail(email)
            .map(UserEntityMapper::toDomain);

        log.trace("Found user by email {}", read);

        return read;
    }

    @Override
    public final Optional<String> findPassword(final String username) {
        final Optional<String> password;

        log.trace("Finding password for user {}", username);

        password = userSpringRepository.findByUsername(username)
            .map(UserEntity::getPassword);

        log.trace("Found password for user {}", username);

        return password;
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

            log.trace("Increased login attempts for user {} to {}", username, attempts);
        } else {
            attempts = -1;
            log.warn("User {} doesn't exist", username);
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
            result = UserEntityMapper.toDomain(updated);

            log.trace("Locked user {}", username);
        } else {
            // TODO: Maybe return an optional
            result = new User(null, null, null, false, false, false, false, null);
            log.warn("User {} doesn't exist", username);
        }

        return result;
    }

    @Override
    public final User resetPassword(final String username, final String password) {
        final Optional<UserEntity> read;
        final UserEntity           user;
        final UserEntity           updated;
        final User                 result;

        log.trace("Resetting pasword for {}", username);

        // TODO: maybe this should be done by the service
        read = userSpringRepository.findByUsername(username);
        if (read.isPresent()) {
            user = read.get();
            user.setPassword(password);

            user.setPasswordNotExpired(true);
            updated = userSpringRepository.save(user);
            result = UserEntityMapper.toDomain(updated);

            log.trace("Resetted pasword for {}", username);
        } else {
            // TODO: Maybe return an optional
            result = new User(null, null, null, false, false, false, false, null);
            log.warn("User {} doesn't exist", username);
        }

        return result;
    }

    @Override
    public final User save(final User user) {
        final Optional<UserEntity> existing;
        final UserEntity           entity;
        final UserEntity           saved;
        final User                 created;

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
        created = UserEntityMapper.toDomain(saved);

        log.trace("Saved user: {}", created);

        return created;
    }

    @Override
    public final User save(final User user, final String password) {
        final UserEntity entity;
        final UserEntity saved;
        final User       created;

        log.trace("Saving user {} with password", user);

        entity = toEntity(user);
        entity.setPassword(password);

        saved = userSpringRepository.save(entity);

        created = UserEntityMapper.toDomain(saved);

        log.trace("Saved user {} with password", created);

        return created;
    }

    private final RoleEntity toEntity(final Role role) {
        final Optional<RoleEntity> read;

        // TODO: move to mapper

        read = roleSpringRepository.findByName(role.name());

        return read.orElse(null);
    }

    private final UserEntity toEntity(final User user) {
        final Collection<RoleEntity> roles;
        final UserEntity             entity;

        // TODO: move to mapper

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

}
