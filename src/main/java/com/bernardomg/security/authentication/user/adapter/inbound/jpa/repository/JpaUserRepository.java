
package com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.model.UserChange;
import com.bernardomg.security.authentication.user.domain.model.UserQuery;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;

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
    public final boolean existsEmail(final String email) {
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
    public final User save(final String username, final UserChange user) {
        final UserEntity           entity;
        final Optional<UserEntity> readUser;
        final Optional<UserEntity> oldRead;
        final UserEntity           old;

        readUser = userRepository.findOneByUsername(username);

        entity = toEntity(user, readUser.get()
            .getId());

        // TODO: should be handled by the model
        // Trim strings
        entity.setName(entity.getName()
            .trim());
        entity.setEmail(entity.getEmail()
            .trim());

        // TODO: should be handled by the model
        // Remove case
        entity.setEmail(entity.getEmail()
            .toLowerCase());

        oldRead = userRepository.findOneByUsername(username);
        if (oldRead.isPresent()) {
            old = oldRead.get();

            // Can't change username by updating
            entity.setUsername(old.getUsername());

            // Can't change password by updating
            entity.setPassword(old.getPassword());

            // Can't change status by updating
            entity.setExpired(old.getExpired());
            entity.setLocked(old.getLocked());
        }

        return toDomain(userRepository.save(entity));
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
