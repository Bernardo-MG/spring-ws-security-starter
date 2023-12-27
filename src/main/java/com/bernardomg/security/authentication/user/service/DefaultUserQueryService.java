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

package com.bernardomg.security.authentication.user.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.exception.MissingUserUsernameException;
import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.model.UserChange;
import com.bernardomg.security.authentication.user.model.UserQuery;
import com.bernardomg.security.authentication.user.persistence.model.UserEntity;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.validation.UpdateUserValidator;
import com.bernardomg.validation.Validator;

import lombok.extern.slf4j.Slf4j;

/**
 * Default user query service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class DefaultUserQueryService implements UserQueryService {

    /**
     * User repository.
     */
    private final UserRepository        userRepository;

    /**
     * Update user validator.
     */
    private final Validator<UserChange> validatorUpdateUser;

    public DefaultUserQueryService(final UserRepository userRepo) {
        super();

        userRepository = Objects.requireNonNull(userRepo);

        validatorUpdateUser = new UpdateUserValidator(userRepo);
    }

    @Override
    public final void delete(final String username) {

        log.debug("Deleting user {}", username);

        if (!userRepository.existsByUsername(username)) {
            throw new MissingUserUsernameException(username);
        }

        userRepository.deleteByUsername(username);
    }

    @Override
    public final Iterable<User> getAll(final UserQuery query, final Pageable page) {
        final UserEntity entity;

        log.debug("Reading users with sample {} and pagination {}", query, page);

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
            .map(this::toDto);
    }

    @Override
    public final Optional<User> getOne(final String username) {

        log.debug("Reading user {}", username);

        // TODO: Use the read optional
        if (!userRepository.existsByUsername(username)) {
            throw new MissingUserUsernameException(username);
        }

        return userRepository.findOneByUsername(username)
            .map(this::toDto);
    }

    @Override
    public final User update(final String username, final UserChange user) {
        final UserEntity           userEntity;
        final UserEntity           created;
        final Optional<UserEntity> oldRead;
        final UserEntity           old;
        final Optional<UserEntity> readUser;

        log.debug("Updating user {} using data {}", username, user);

        readUser = userRepository.findOneByUsername(username);

        if (readUser.isEmpty()) {
            throw new MissingUserUsernameException(username);
        }

        validatorUpdateUser.validate(user);

        userEntity = toEntity(user, readUser.get()
            .getId());

        // Trim strings
        userEntity.setName(userEntity.getName()
            .trim());
        userEntity.setEmail(userEntity.getEmail()
            .trim());

        // Remove case
        userEntity.setEmail(userEntity.getEmail()
            .toLowerCase());

        oldRead = userRepository.findOneByUsername(username);
        if (oldRead.isPresent()) {
            old = oldRead.get();

            // Can't change username by updating
            userEntity.setUsername(old.getUsername());

            // Can't change password by updating
            userEntity.setPassword(old.getPassword());

            // Can't change status by updating
            userEntity.setExpired(old.getExpired());
            userEntity.setLocked(old.getLocked());
        }

        created = userRepository.save(userEntity);

        return toDto(created);
    }

    private final User toDto(final UserEntity user) {
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

    private final UserEntity toEntity(final UserChange user, final long id) {
        return UserEntity.builder()
            .withId(id)
            .withName(user.getName())
            .withEmail(user.getEmail())
            .withEnabled(user.getEnabled())
            .withPasswordExpired(user.getPasswordExpired())
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
