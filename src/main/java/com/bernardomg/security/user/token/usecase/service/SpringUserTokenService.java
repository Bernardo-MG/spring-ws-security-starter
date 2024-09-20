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

package com.bernardomg.security.user.token.usecase.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.user.token.domain.exception.MissingUserTokenException;
import com.bernardomg.security.user.token.domain.model.UserToken;
import com.bernardomg.security.user.token.domain.model.UserTokenPatch;
import com.bernardomg.security.user.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.user.token.usecase.validation.UserTokenNotExpiredRule;
import com.bernardomg.security.user.token.usecase.validation.UserTokenNotRevokedRule;
import com.bernardomg.validation.validator.FieldRuleValidator;
import com.bernardomg.validation.validator.Validator;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring-based implementation of the user token service.
 * <h2>Unusable tokens</h2>
 * <p>
 * Cleaning up tokens removes all of these:
 * <ul>
 * <li>Consumed tokens</li>
 * <li>Revoked tokens</li>
 * <li>Expired tokens</li>
 * </ul>
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
@Transactional
public final class SpringUserTokenService implements UserTokenService {

    /**
     * User token repository.
     */
    private final UserTokenRepository       userTokenRepository;

    /**
     * Patch validator.
     */
    private final Validator<UserTokenPatch> validatorPatch;

    public SpringUserTokenService(final UserTokenRepository userTokenRepo) {
        super();

        userTokenRepository = Objects.requireNonNull(userTokenRepo);

        validatorPatch = new FieldRuleValidator<>(new UserTokenNotExpiredRule(), new UserTokenNotRevokedRule());
    }

    @Override
    public final void cleanUpTokens() {
        final Collection<UserToken> tokens;
        final Collection<String>    tokenCodes;

        // Expiration date before now
        // Revoked
        // Consumed
        tokens = userTokenRepository.findAllFinished();

        log.info("Removing {} finished tokens", tokens.size());

        tokenCodes = tokens.stream()
            .map(UserToken::getToken)
            .toList();
        userTokenRepository.deleteAll(tokenCodes);
    }

    @Override
    public final Iterable<UserToken> getAll(final Pageable pagination) {
        return userTokenRepository.findAll(pagination);
    }

    @Override
    public final Optional<UserToken> getOne(final String token) {
        final Optional<UserToken> userToken;

        log.debug("Reading token {}", token);

        userToken = userTokenRepository.findOne(token);
        if (userToken.isEmpty()) {
            log.error("Missing user token {}", token);
            throw new MissingUserTokenException(token);
        }

        return userToken;
    }

    @Override
    public final UserToken patch(final UserTokenPatch token) {
        final UserToken readToken;
        final UserToken toSave;

        log.debug("Patching token {}", token.getToken());

        readToken = userTokenRepository.findOne(token.getToken())
            .orElseThrow(() -> {
                log.error("Missing user token {}", token.getToken());
                return new MissingUserTokenException(token.getToken());
            });

        validatorPatch.validate(token);

        toSave = copy(readToken, token);

        return userTokenRepository.save(toSave);
    }

    private final UserToken copy(final UserToken existing, final UserTokenPatch updated) {
        final LocalDateTime expirationDate;
        final Boolean       revoked;

        if (updated.getExpirationDate() == null) {
            expirationDate = existing.getExpirationDate();
        } else {
            expirationDate = updated.getExpirationDate();
        }
        if (updated.getRevoked() == null) {
            revoked = existing.isRevoked();
        } else {
            revoked = updated.getRevoked();
        }

        return UserToken.builder()
            .withUsername(existing.getUsername())
            .withName(existing.getName())
            .withScope(existing.getScope())
            .withToken(existing.getToken())
            .withCreationDate(existing.getCreationDate())
            .withExpirationDate(expirationDate)
            .withConsumed(existing.isConsumed())
            .withRevoked(revoked)
            .build();
    }

}
