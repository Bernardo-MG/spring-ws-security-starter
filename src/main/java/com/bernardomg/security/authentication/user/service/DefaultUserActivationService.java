
package com.bernardomg.security.authentication.user.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.user.exception.EnabledUserException;
import com.bernardomg.security.authentication.user.exception.ExpiredUserException;
import com.bernardomg.security.authentication.user.exception.LockedUserException;
import com.bernardomg.security.authentication.user.exception.UserNotFoundException;
import com.bernardomg.security.authentication.user.model.ImmutableUser;
import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.model.query.UserCreate;
import com.bernardomg.security.authentication.user.persistence.model.PersistentUser;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.validation.CreateUserValidator;
import com.bernardomg.security.email.sender.SecurityMessageSender;
import com.bernardomg.security.user.token.exception.InvalidTokenException;
import com.bernardomg.security.user.token.model.ImmutableUserTokenStatus;
import com.bernardomg.security.user.token.model.UserTokenStatus;
import com.bernardomg.security.user.token.store.UserTokenStore;
import com.bernardomg.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DefaultUserActivationService implements UserActivationService {

    /**
     * Message sender. Registering new users may require emails, or other kind of messaging.
     */
    private final SecurityMessageSender messageSender;

    /**
     * Password encoder, for validating passwords.
     */
    private final PasswordEncoder       passwordEncoder;

    /**
     * Token processor.
     */
    private final UserTokenStore        tokenStore;

    private final UserRepository        userRepository;

    private final Validator<UserCreate> validatorCreateUser;

    public DefaultUserActivationService(final UserRepository userRepo, final SecurityMessageSender mSender,
            final UserTokenStore tStore, final PasswordEncoder passEncoder) {
        super();

        userRepository = Objects.requireNonNull(userRepo);

        tokenStore = Objects.requireNonNull(tStore);

        passwordEncoder = Objects.requireNonNull(passEncoder);

        messageSender = Objects.requireNonNull(mSender);

        validatorCreateUser = new CreateUserValidator(userRepo);
    }

    @Override
    public final User activateNewUser(final String token, final String password) {
        final String         tokenUsername;
        final PersistentUser user;
        final String         encodedPassword;

        tokenStore.validate(token);

        tokenUsername = tokenStore.getUsername(token);

        log.debug("Enabling new user {}", tokenUsername);

        user = getUserByUsername(tokenUsername);

        authorizeEnableUser(user);

        user.setEnabled(true);
        user.setPasswordExpired(false);
        encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);

        userRepository.save(user);
        tokenStore.consumeToken(token);

        log.debug("Enabled new user {}", tokenUsername);

        return toDto(user);
    }

    @Override
    public final User registerNewUser(final UserCreate user) {
        final PersistentUser userEntity;
        final PersistentUser created;
        final String         token;

        log.debug("Registering new user {} with email {}", user.getUsername(), user.getEmail());

        validatorCreateUser.validate(user);

        userEntity = toEntity(user);

        // Trim strings
        userEntity.setName(userEntity.getName()
            .trim());
        userEntity.setUsername(userEntity.getUsername()
            .trim());
        userEntity.setEmail(userEntity.getEmail()
            .trim());

        // Remove case
        userEntity.setUsername(userEntity.getUsername()
            .toLowerCase());
        userEntity.setEmail(userEntity.getEmail()
            .toLowerCase());

        // TODO: Handle this better, disable until it has a password
        // TODO: Should be the DB default value
        userEntity.setPassword("");

        // Disabled by default
        userEntity.setEnabled(false);
        userEntity.setExpired(false);
        userEntity.setLocked(false);
        userEntity.setPasswordExpired(true);

        created = userRepository.save(userEntity);

        // Revoke previous tokens
        tokenStore.revokeExistingTokens(created.getUsername());

        // Register new token
        token = tokenStore.createToken(created.getUsername());

        // TODO: Handle through events
        messageSender.sendUserRegisteredMessage(created.getEmail(), user.getUsername(), token);

        log.debug("Registered new user {} with email {}", user.getUsername(), user.getEmail());

        return toDto(created);
    }

    @Override
    public final UserTokenStatus validateToken(final String token) {
        boolean      valid;
        final String username;

        try {
            tokenStore.validate(token);
            valid = true;
        } catch (final InvalidTokenException ex) {
            valid = false;
        }
        username = tokenStore.getUsername(token);

        return ImmutableUserTokenStatus.builder()
            .valid(valid)
            .username(username)
            .build();
    }

    /**
     * Authenticates the new user enabling attempt. If the user is not authenticated, then an exception is thrown.
     *
     * @param user
     *            user for which the password is changed
     */
    private final void authorizeEnableUser(final PersistentUser user) {
        if (user.getExpired()) {
            log.error("Can't enable new user. User {} is expired", user.getUsername());
            throw new ExpiredUserException(user.getUsername());
        }
        if (user.getLocked()) {
            log.error("Can't enable new user. User {} is locked", user.getUsername());
            throw new LockedUserException(user.getUsername());
        }
        if (user.getEnabled()) {
            log.error("Can't enable new user. User {} is already enabled", user.getUsername());
            throw new EnabledUserException(user.getUsername());
        }
    }

    private final PersistentUser getUserByUsername(final String username) {
        final Optional<PersistentUser> user;

        user = userRepository.findOneByUsername(username);

        // Validate the user exists
        if (!user.isPresent()) {
            log.error("Couldn't enable new user {}, as it doesn't exist", username);
            throw new UserNotFoundException(username);
        }

        return user.get();
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

    private final PersistentUser toEntity(final UserCreate user) {
        return PersistentUser.builder()
            .username(user.getUsername())
            .name(user.getName())
            .email(user.getEmail())
            .build();
    }

}
