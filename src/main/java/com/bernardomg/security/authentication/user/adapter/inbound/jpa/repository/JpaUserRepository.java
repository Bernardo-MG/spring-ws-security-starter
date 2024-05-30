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

package com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.model.UserQuery;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ResourcePermissionEntity;
import com.bernardomg.security.authorization.permission.domain.comparator.ResourcePermissionComparator;
import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RolePermissionEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleSpringRepository;
import com.bernardomg.security.authorization.role.domain.comparator.RoleComparator;
import com.bernardomg.security.authorization.role.domain.model.Role;

/**
 * User repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Transactional
public final class JpaUserRepository implements UserRepository {

    /**
     * Password encoder, for validating passwords.
     */
    private final PasswordEncoder      passwordEncoder;

    private final RoleSpringRepository roleSpringRepository;

    /**
     * User repository.
     */
    private final UserSpringRepository userSpringRepository;

    public JpaUserRepository(final UserSpringRepository userSpringRepo, final RoleSpringRepository roleSpringRepo,
            final PasswordEncoder passEncoder) {
        super();

        userSpringRepository = userSpringRepo;
        roleSpringRepository = roleSpringRepo;
        passwordEncoder = Objects.requireNonNull(passEncoder);
    }

    @Override
    public final void clearLoginAttempts(final String username) {
        final Optional<UserEntity> existing;
        final UserEntity           toSave;

        existing = userSpringRepository.findByUsername(username);
        if (existing.isPresent()) {
            toSave = existing.get();
            toSave.setLoginAttempts(0);

            userSpringRepository.save(toSave);
        }
    }

    @Override
    public final void delete(final String username) {
        userSpringRepository.deleteByUsername(username);
    }

    @Override
    public final boolean exists(final String username) {
        return userSpringRepository.existsByUsername(username);
    }

    @Override
    public final boolean existsByEmail(final String email) {
        return userSpringRepository.existsByEmail(email);
    }

    @Override
    public final boolean existsEmailForAnotherUser(final String username, final String email) {
        return userSpringRepository.existsByUsernameNotAndEmail(username, email);
    }

    @Override
    public final Iterable<User> findAll(final UserQuery query, final Pageable page) {
        final UserEntity entity;

        entity = toEntity(query);
        return userSpringRepository.findAll(Example.of(entity), page)
            .map(this::toDomain);
    }

    @Override
    public final Optional<User> findOne(final String username) {
        return userSpringRepository.findByUsername(username)
            .map(this::toDomain);
    }

    @Override
    public final Optional<User> findOneByEmail(final String email) {
        return userSpringRepository.findByEmail(email)
            .map(this::toDomain);
    }

    @Override
    public final Optional<String> findPassword(final String username) {
        return userSpringRepository.findByUsername(username)
            .map(UserEntity::getPassword);
    }

    @Override
    public final int getLoginAttempts(final String username) {
        return userSpringRepository.findByUsername(username)
            .map(UserEntity::getLoginAttempts)
            .orElse(0);
    }

    @Override
    public final int increaseLoginAttempts(final String username) {
        final Optional<UserEntity> existing;
        final UserEntity           toSave;
        final int                  attempts;

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

        read = userSpringRepository.findByUsername(username);
        if (read.isPresent()) {
            user = read.get();
            user.setLocked(true);
            updated = userSpringRepository.save(user);
            result = toDomain(updated);
        } else {
            // TODO: Maybe return an optional
            result = User.builder()
                .build();
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

        read = userSpringRepository.findByUsername(username);
        if (read.isPresent()) {
            user = read.get();

            // Encode password
            encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);

            user.setPasswordExpired(false);
            updated = userSpringRepository.save(user);
            result = toDomain(updated);
        } else {
            // TODO: Maybe return an optional
            result = User.builder()
                .build();
        }

        return result;
    }

    @Override
    public final User save(final User user, final String password) {
        final Optional<UserEntity> existing;
        final String               encodedPassword;
        final UserEntity           entity;
        final UserEntity           saved;

        entity = toEntity(user);

        existing = userSpringRepository.findByUsername(user.getUsername());
        if (existing.isPresent()) {
            entity.setId(existing.get()
                .getId());
            entity.setLoginAttempts(existing.get()
                .getLoginAttempts());
        }

        encodedPassword = passwordEncoder.encode(password);
        entity.setPassword(encodedPassword);

        saved = userSpringRepository.save(entity);

        return toDomain(saved);
    }

    @Override
    public final User update(final User user) {
        final Optional<UserEntity> existing;
        final UserEntity           entity;
        final UserEntity           saved;
        final User                 result;

        entity = toEntity(user);

        existing = userSpringRepository.findByUsername(user.getUsername());
        if (existing.isPresent()) {
            entity.setId(existing.get()
                .getId());
            entity.setPassword(existing.get()
                .getPassword());
            entity.setLoginAttempts(existing.get()
                .getLoginAttempts());

            saved = userSpringRepository.save(entity);
            result = toDomain(saved);
        } else {
            // TODO: if it doesn't exist, then maybe create
            result = null;
        }

        return result;
    }

    private final ResourcePermission toDomain(final ResourcePermissionEntity entity) {
        return ResourcePermission.builder()
            .withResource(entity.getResource())
            .withAction(entity.getAction())
            .build();
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
        return Role.builder()
            .withName(role.getName())
            .withPermissions(permissions)
            .build();
    }

    private final User toDomain(final UserEntity user) {
        final Collection<Role> roles;

        roles = user.getRoles()
            .stream()
            .filter(Objects::nonNull)
            .map(this::toDomain)
            .sorted(new RoleComparator())
            .toList();
        return User.builder()
            .withUsername(user.getUsername())
            .withName(user.getName())
            .withEmail(user.getEmail())
            .withEnabled(user.getEnabled())
            .withExpired(user.getExpired())
            .withLocked(user.getLocked())
            .withPasswordExpired(user.getPasswordExpired())
            .withRoles(roles)
            .build();
    }

    private final RoleEntity toEntity(final Role role) {
        final Optional<RoleEntity> read;

        read = roleSpringRepository.findByName(role.getName());

        return read.orElse(null);
    }

    private final UserEntity toEntity(final User user) {
        final Collection<RoleEntity> roles;

        roles = user.getRoles()
            .stream()
            .map(this::toEntity)
            .filter(Objects::nonNull)
            .toList();
        return UserEntity.builder()
            .withUsername(user.getUsername())
            .withName(user.getName())
            .withEmail(user.getEmail())
            .withEnabled(user.isEnabled())
            .withExpired(user.isExpired())
            .withLocked(user.isLocked())
            .withPasswordExpired(user.isPasswordExpired())
            .withRoles(roles)
            .withLoginAttempts(0)
            .build();
    }

    private final UserEntity toEntity(final UserQuery user) {
        return UserEntity.builder()
            .withUsername(user.getUsername())
            .withName(user.getName())
            .withEmail(user.getEmail())
            .withEnabled(user.getEnabled())
            .withExpired(user.getExpired())
            .withLocked(user.getLocked())
            .withPasswordExpired(user.getPasswordExpired())
            .build();
    }

}
