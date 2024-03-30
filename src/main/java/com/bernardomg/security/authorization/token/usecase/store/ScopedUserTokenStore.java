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

package com.bernardomg.security.authorization.token.usecase.store;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.bernardomg.security.authentication.user.domain.exception.MissingUserException;
import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authorization.token.domain.exception.ConsumedTokenException;
import com.bernardomg.security.authorization.token.domain.exception.ExpiredTokenException;
import com.bernardomg.security.authorization.token.domain.exception.MissingUserTokenException;
import com.bernardomg.security.authorization.token.domain.exception.OutOfScopeTokenException;
import com.bernardomg.security.authorization.token.domain.exception.RevokedTokenException;
import com.bernardomg.security.authorization.token.domain.model.UserToken;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * User token store which handles a scope for the tokens. This scope allows keeping a single table for all the tokens,
 * while isolating the usecases.
 * <h2>Validity</h2>
 * <p>
 * The token validity duration is received in the constructor. This sets the validity duration, starting on the moment
 * it is created.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class ScopedUserTokenStore implements UserTokenStore {

    /**
     * Token scope.
     */
    private final String              tokenScope;

    /**
     * User repository.
     */
    private final UserRepository      userRepository;

    /**
     * User tokens repository.
     */
    private final UserTokenRepository userTokenRepository;

    /**
     * Token validity duration. This is how long the token is valid, starting on the time it is created.
     */
    private final Duration            validity;

    public ScopedUserTokenStore(final UserTokenRepository tokenRepo, final UserRepository userRepo, final String scope,
            final Duration duration) {
        super();

        userTokenRepository = Objects.requireNonNull(tokenRepo);
        userRepository = Objects.requireNonNull(userRepo);
        tokenScope = Objects.requireNonNull(scope);
        validity = Objects.requireNonNull(duration);
    }

    @Override
    public final void consumeToken(final String token) {
        final Optional<UserToken> read;
        final UserToken           tokenData;

        read = userTokenRepository.findOneByScope(token, tokenScope);

        if (!read.isPresent()) {
            log.error("Token missing: {}", token);
            throw new MissingUserTokenException(token);
        }

        tokenData = read.get();
        if (tokenData.getConsumed()) {
            log.warn("Token already consumed: {}", token);
            throw new ConsumedTokenException(token);
        }

        tokenData.setConsumed(true);
        userTokenRepository.save(tokenData);
        log.debug("Consumed token {}", token);
    }

    @Override
    public final String createToken(final String username) {
        final UserToken     token;
        final LocalDateTime creation;
        final LocalDateTime expiration;
        final String        tokenCode;
        final boolean       exists;

        exists = userRepository.exists(username);
        if (!exists) {
            throw new MissingUserException(username);
        }

        creation = LocalDateTime.now();
        expiration = creation.plus(validity);

        tokenCode = UUID.randomUUID()
            .toString();

        token = UserToken.builder()
            .withUsername(username)
            .withScope(tokenScope)
            .withCreationDate(creation)
            .withToken(tokenCode)
            .withConsumed(false)
            .withRevoked(false)
            .withExpirationDate(expiration)
            .build();

        userTokenRepository.save(token);

        log.debug("Created token for {} with scope {}", username, tokenScope);

        return tokenCode;
    }

    @Override
    public final String getUsername(final String token) {
        final Optional<String> username;

        username = userTokenRepository.findOneByScope(token, tokenScope)
            .map(UserToken::getUsername);

        if (username.isEmpty()) {
            throw new MissingUserTokenException(token);
        }

        return username.get();
    }

    @Override
    public final void revokeExistingTokens(final String username) {
        final Collection<UserToken> toRevoke;
        final Optional<User>        readUser;
        final User                  user;

        readUser = userRepository.findOne(username);
        if (!readUser.isPresent()) {
            throw new MissingUserException(username);
        }

        user = readUser.get();

        // Find all tokens not revoked, and mark them as revoked
        toRevoke = userTokenRepository.findAllNotRevoked(user.getUsername(), tokenScope);
        toRevoke.forEach(t -> t.setRevoked(true));

        userTokenRepository.saveAll(toRevoke);

        log.debug("Revoked all existing tokens with scope {} for {}", tokenScope, user.getUsername());
    }

    @Override
    public final void validate(final String token) {
        final Optional<UserToken> read;
        final UserToken           entity;

        read = userTokenRepository.findOne(token);
        if (!read.isPresent()) {
            log.warn("Token not registered: {}", token);
            throw new MissingUserTokenException(token);
        }

        entity = read.get();
        if (!tokenScope.equals(entity.getScope())) {
            // Scope mismatch
            log.warn("Expected scope {}, but the token is for {}", tokenScope, entity.getScope());
            throw new OutOfScopeTokenException(token, tokenScope, entity.getScope());
        }
        if (entity.getConsumed()) {
            // Consumed
            // It isn't a valid token
            log.warn("Consumed token: {}", token);
            throw new ConsumedTokenException(token);
        }
        if (entity.getRevoked()) {
            // Revoked
            // It isn't a valid token
            log.warn("Revoked token: {}", token);
            throw new RevokedTokenException(token);
        }
        if (LocalDateTime.now()
            .isAfter(entity.getExpirationDate())) {
            // Expired
            // It isn't a valid token
            log.warn("Expired token: {}", token);
            throw new ExpiredTokenException(token);
        }
    }

}
