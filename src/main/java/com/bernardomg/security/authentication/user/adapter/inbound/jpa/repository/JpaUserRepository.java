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

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.model.UserQuery;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;

/**
 * User repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
public final class JpaUserRepository implements UserRepository {

    /**
     * Password encoder, for validating passwords.
     */
    private final PasswordEncoder      passwordEncoder;

    /**
     * User repository.
     */
    private final UserSpringRepository userRepository;

    public JpaUserRepository(final UserSpringRepository userRepo, final PasswordEncoder passEncoder) {
        super();

        userRepository = userRepo;
        passwordEncoder = Objects.requireNonNull(passEncoder);
    }

    @Override
    public final void delete(final String username) {
        userRepository.deleteByUsername(username);
    }

    @Override
    public final boolean exists(final String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public final boolean existsByEmail(final String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public final boolean existsEmailForAnotherUser(final String username, final String email) {
        return userRepository.existsByUsernameNotAndEmail(username, email);
    }

    @Override
    public final Iterable<User> findAll(final UserQuery query, final Pageable page) {
        final UserEntity entity;

        entity = toEntity(query);
        if (entity.getUsername() != null) {
            entity.setUsername(entity.getUsername()
                .toLowerCase());
        }
        if (entity.getEmail() != null) {
            entity.setEmail(entity.getEmail()
                .toLowerCase());
        }

        return userRepository.findAll(Example.of(entity), page)
            .map(this::toDomain);
    }

    @Override
    public final Optional<User> findOne(final String username) {
        return userRepository.findOneByUsername(username)
            .map(this::toDomain);
    }

    @Override
    public final Optional<User> findOneByEmail(final String email) {
        return userRepository.findOneByEmail(email)
            .map(this::toDomain);
    }

    @Override
    public final Optional<String> findPassword(final String username) {
        return userRepository.findOneByUsername(username)
            .map(UserEntity::getPassword);
    }

    @Override
    public final User save(final User user, final String password) {
        final Optional<UserEntity> existing;
        final String               encodedPassword;
        final UserEntity           entity;
        final UserEntity           saved;

        entity = toEntity(user);

        existing = userRepository.findOneByUsername(user.getUsername());
        if (existing.isPresent()) {
            entity.setId(existing.get()
                .getId());
        }

        encodedPassword = passwordEncoder.encode(password);
        entity.setPassword(encodedPassword);

        saved = userRepository.save(entity);

        return toDomain(saved);
    }

    @Override
    public final User update(final User user) {
        final Optional<UserEntity> existing;
        final UserEntity           entity;
        final UserEntity           saved;
        final User                 result;

        entity = toEntity(user);

        existing = userRepository.findOneByUsername(user.getUsername());
        if (existing.isPresent()) {
            entity.setId(existing.get()
                .getId());
            entity.setPassword(existing.get()
                .getPassword());

            saved = userRepository.save(entity);
            result = toDomain(saved);
        } else {
            result = null;
        }

        return result;
    }

    private final User toDomain(final UserEntity user) {
        return User.builder()
            .withUsername(user.getUsername())
            .withName(user.getName())
            .withEmail(user.getEmail())
            .withEnabled(user.getEnabled())
            .withExpired(user.getExpired())
            .withLocked(user.getLocked())
            .withPasswordExpired(user.getPasswordExpired())
            .build();
    }

    private final UserEntity toEntity(final User user) {
        return UserEntity.builder()
            .withUsername(user.getUsername())
            .withName(user.getName())
            .withEmail(user.getEmail())
            .withEnabled(user.isEnabled())
            .withExpired(user.isExpired())
            .withLocked(user.isLocked())
            .withPasswordExpired(user.isPasswordExpired())
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
