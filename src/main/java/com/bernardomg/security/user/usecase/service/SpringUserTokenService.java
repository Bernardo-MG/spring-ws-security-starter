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

package com.bernardomg.security.user.usecase.service;

import java.time.Instant;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.user.domain.exception.MissingUserTokenException;
import com.bernardomg.security.user.domain.model.UserToken;
import com.bernardomg.security.user.domain.repository.UserTokenRepository;
import com.bernardomg.security.user.usecase.validation.UserTokenExpirationDateNotInPastRule;
import com.bernardomg.security.user.usecase.validation.UserTokenPatchNotRevokedRule;
import com.bernardomg.validation.validator.FieldRuleValidator;
import com.bernardomg.validation.validator.Validator;

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
@Transactional
public final class SpringUserTokenService implements UserTokenService {

    /**
     * Logger for the class.
     */
    private static final Logger        log = LoggerFactory.getLogger(SpringUserTokenService.class);

    /**
     * User token repository.
     */
    private final UserTokenRepository  userTokenRepository;

    /**
     * Patch validator.
     */
    private final Validator<UserToken> validatorPatch;

    public SpringUserTokenService(final UserTokenRepository userTokenRepo) {
        super();

        userTokenRepository = Objects.requireNonNull(userTokenRepo);

        validatorPatch = new FieldRuleValidator<>(new UserTokenExpirationDateNotInPastRule(), new UserTokenPatchNotRevokedRule());
    }

    @Override
    public final void cleanUpTokens() {
        final Collection<UserToken> tokens;
        final Collection<String>    tokenCodes;

        log.trace("Cleaning up tokens");

        // Expiration date before now
        // Revoked
        // Consumed
        tokens = userTokenRepository.findAllFinished();

        log.info("Removing {} finished tokens", tokens.size());

        tokenCodes = tokens.stream()
            .map(UserToken::token)
            .toList();
        userTokenRepository.deleteAll(tokenCodes);

        log.trace("Cleaned up tokens");
    }

    @Override
    public final Page<UserToken> getAll(final Pagination pagination, final Sorting sorting) {
        final Page<UserToken> tokens;

        log.trace("Reading tokens with pagination {} and sorting {}", pagination, sorting);

        tokens = userTokenRepository.findAll(pagination, sorting);

        log.trace("Read tokens with pagination {} and sorting {}", pagination, sorting);

        return tokens;
    }

    @Override
    public final Optional<UserToken> getOne(final String token) {
        final Optional<UserToken> userToken;

        log.trace("Reading token {}", token);

        userToken = userTokenRepository.findOne(token);
        if (userToken.isEmpty()) {
            log.error("Missing user token {}", token);
            throw new MissingUserTokenException(token);
        }

        log.trace("Read token {}", token);

        return userToken;
    }

    @Override
    public final UserToken patch(final UserToken token) {
        final UserToken existing;
        final UserToken toSave;

        log.debug("Patching token {}", token.token());

        existing = userTokenRepository.findOne(token.token())
            .orElseThrow(() -> {
                log.error("Missing user token {}", token.token());
                return new MissingUserTokenException(token.token());
            });

        validatorPatch.validate(token);

        toSave = copy(existing, token);

        return userTokenRepository.save(toSave);
    }

    private final UserToken copy(final UserToken existing, final UserToken updated) {
        final Instant expirationDate;
        final Boolean revoked;

        if (updated.expirationDate() == null) {
            expirationDate = existing.expirationDate();
        } else {
            expirationDate = updated.expirationDate();
        }
        if (updated.revoked() == null) {
            revoked = existing.revoked();
        } else {
            revoked = updated.revoked();
        }

        return new UserToken(existing.username(), existing.name(), existing.scope(), existing.token(),
            existing.creationDate(), expirationDate, existing.consumed(), revoked);
    }

}
