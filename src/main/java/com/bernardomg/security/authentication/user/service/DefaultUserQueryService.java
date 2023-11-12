
package com.bernardomg.security.authentication.user.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.exception.MissingUserIdException;
import com.bernardomg.security.authentication.user.model.ImmutableUser;
import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.model.query.UserQuery;
import com.bernardomg.security.authentication.user.model.query.UserUpdate;
import com.bernardomg.security.authentication.user.persistence.model.PersistentUser;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.validation.UpdateUserValidator;
import com.bernardomg.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DefaultUserQueryService implements UserQueryService {

    private final UserRepository        userRepository;

    private final Validator<UserUpdate> validatorUpdateUser;

    public DefaultUserQueryService(final UserRepository userRepo) {
        super();

        userRepository = Objects.requireNonNull(userRepo);

        validatorUpdateUser = new UpdateUserValidator(userRepo);
    }

    @Override
    public final void delete(final long userId) {

        log.debug("Deleting user {}", userId);

        if (!userRepository.existsById(userId)) {
            throw new MissingUserIdException(userId);
        }

        userRepository.deleteById(userId);
    }

    @Override
    public final Iterable<User> getAll(final UserQuery sample, final Pageable pageable) {
        final PersistentUser entity;

        log.debug("Reading users with sample {} and pagination {}", sample, pageable);

        entity = toEntity(sample);
        if (entity.getUsername() != null) {
            entity.setUsername(entity.getUsername()
                .toLowerCase());
        }
        if (entity.getEmail() != null) {
            entity.setEmail(entity.getEmail()
                .toLowerCase());
        }

        return userRepository.findAll(Example.of(entity), pageable)
            .map(this::toDto);
    }

    @Override
    public final Optional<User> getOne(final long id) {

        log.debug("Reading member with id {}", id);

        // TODO: Use the read optional
        if (!userRepository.existsById(id)) {
            throw new MissingUserIdException(id);
        }

        return userRepository.findById(id)
            .map(this::toDto);
    }

    @Override
    public final User update(final long id, final UserUpdate user) {
        final PersistentUser           userEntity;
        final PersistentUser           created;
        final Optional<PersistentUser> oldRead;
        final PersistentUser           old;

        log.debug("Updating user with id {} using data {}", id, user);

        if (!userRepository.existsById(id)) {
            throw new MissingUserIdException(id);
        }

        validatorUpdateUser.validate(user);

        userEntity = toEntity(user);

        // Trim strings
        userEntity.setName(userEntity.getName()
            .trim());
        userEntity.setEmail(userEntity.getEmail()
            .trim());

        // Remove case
        userEntity.setEmail(userEntity.getEmail()
            .toLowerCase());

        oldRead = userRepository.findById(user.getId());
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

    private final User toDto(final PersistentUser user) {
        return ImmutableUser.builder()
            .id(user.getId())
            .username(user.getUsername())
            .name(user.getName())
            .email(user.getEmail())
            .enabled(user.getEnabled())
            .expired(user.getExpired())
            .locked(user.getLocked())
            .passwordExpired(user.getPasswordExpired())
            .build();
    }

    private final PersistentUser toEntity(final UserQuery user) {
        return PersistentUser.builder()
            .username(user.getUsername())
            .name(user.getName())
            .email(user.getEmail())
            .enabled(user.getEnabled())
            .expired(user.getExpired())
            .locked(user.getLocked())
            .passwordExpired(user.getPasswordExpired())
            .build();
    }

    private final PersistentUser toEntity(final UserUpdate user) {
        return PersistentUser.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .enabled(user.getEnabled())
            .passwordExpired(user.getPasswordExpired())
            .build();
    }

}
