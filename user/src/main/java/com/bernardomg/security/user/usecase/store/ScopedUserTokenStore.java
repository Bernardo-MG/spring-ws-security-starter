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

package com.bernardomg.security.user.usecase.store;

import java.time.Duration;
import java.util.Collection;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bernardomg.security.user.domain.exception.ConsumedTokenException;
import com.bernardomg.security.user.domain.exception.MissingUserTokenException;
import com.bernardomg.security.user.domain.exception.MissingUsernameException;
import com.bernardomg.security.user.domain.model.User;
import com.bernardomg.security.user.domain.model.UserToken;
import com.bernardomg.security.user.domain.repository.UserRepository;
import com.bernardomg.security.user.domain.repository.UserTokenRepository;

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
public final class ScopedUserTokenStore implements UserTokenStore {

    /**
     * Logger for the class.
     */
    private static final Logger       log = LoggerFactory.getLogger(ScopedUserTokenStore.class);

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
        // TODO: maybe the scope should be received in the method
        tokenScope = Objects.requireNonNull(scope);
        validity = Objects.requireNonNull(duration);
    }

    @Override
    public final void consumeToken(final String token) {
        final UserToken readToken;
        final UserToken updated;

        log.trace("Consuming token with scope {}", tokenScope);

        readToken = userTokenRepository.findOneByScope(token, tokenScope)
            .orElseThrow(() -> {
                log.error("Token missing with scope {}: {}", tokenScope, token);
                throw new MissingUserTokenException(token);
            });

        if (readToken.consumed()) {
            log.warn("Token already consumed with scope {}: {}", tokenScope, token);
            throw new ConsumedTokenException(token);
        }

        updated = readToken.consume();
        userTokenRepository.save(updated);
        log.trace("Consumed token {} with scope {}", token, tokenScope);
    }

    @Override
    public final String createToken(final String username) {
        final UserToken token;

        log.trace("Creating token with scope {} for {}", tokenScope, username);

        if (!userRepository.exists(username)) {
            log.error("Missing user {}", username);
            throw new MissingUsernameException(username);
        }

        token = UserToken.create(username, tokenScope, validity);

        userTokenRepository.save(token);

        log.trace("Created token with scope {} for {}", tokenScope, username);

        return token.token();
    }

    @Override
    public final String getUsername(final String token) {

        log.trace("Getting username from token with scope {}", tokenScope);

        return userTokenRepository.findOneByScope(token, tokenScope)
            .map(UserToken::username)
            .orElseThrow(() -> {
                log.error("Missing user token {}", token);
                throw new MissingUserTokenException(token);
            });
    }

    @Override
    public final void revokeExistingTokens(final String username) {
        final Collection<UserToken> tokens;
        final Collection<UserToken> revoked;
        final User                  readUser;

        log.trace("Revoking tokens with scope {} for {}", tokenScope, username);

        readUser = userRepository.findOne(username)
            .orElseThrow(() -> {
                log.error("Missing user {}", username);
                throw new MissingUsernameException(username);
            });

        // Find all tokens not revoked, and mark them as revoked
        tokens = userTokenRepository.findAllNotRevoked(readUser.username(), tokenScope);
        revoked = tokens.stream()
            .map(UserToken::revoke)
            .toList();

        log.debug("Found {} tokens to revoke with scope {}", revoked.size(), tokenScope);

        userTokenRepository.saveAll(revoked);

        log.trace("Revoked all existing tokens with scope {} for {}", tokenScope, readUser.username());
    }

    @Override
    public final void validate(final String token) {
        final UserToken read;

        log.trace("Validating token with scope {}", tokenScope);

        read = userTokenRepository.findOne(token)
            .orElseThrow(() -> {
                log.warn("Token not registered with scope {}: {}", tokenScope, token);
                throw new MissingUserTokenException(token);
            });

        read.checkStatus(tokenScope);
    }

}
