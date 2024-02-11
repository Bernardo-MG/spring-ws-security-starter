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

package com.bernardomg.security.authorization.token.usecase.service;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.token.domain.exception.MissingUserTokenCodeException;
import com.bernardomg.security.authorization.token.domain.model.UserToken;
import com.bernardomg.security.authorization.token.domain.model.request.UserTokenPartial;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.usecase.validation.PatchUserTokenValidator;
import com.bernardomg.validation.Validator;

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
public final class SpringUserTokenService implements UserTokenService {

    /**
     * User token repository.
     */
    private final UserTokenRepository         userTokenRepository;

    /**
     * Patch validator.
     */
    private final Validator<UserTokenPartial> validatorPatch;

    public SpringUserTokenService(final UserTokenRepository userTokenRepo) {
        super();

        userTokenRepository = Objects.requireNonNull(userTokenRepo);

        validatorPatch = new PatchUserTokenValidator();
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
            throw new MissingUserTokenCodeException(token);
        }

        return userToken;
    }

    @Override
    public final UserToken patch(final String token, final UserTokenPartial partial) {
        final Optional<UserToken> readToken;
        final UserToken           toSave;

        log.debug("Patching token {}", token);

        readToken = userTokenRepository.findOne(token);
        if (readToken.isEmpty()) {
            throw new MissingUserTokenCodeException(token);
        }

        validatorPatch.validate(partial);

        toSave = readToken.get();

        if (partial.getExpirationDate() != null) {
            toSave.setExpirationDate(partial.getExpirationDate());
        }
        if (partial.getRevoked() != null) {
            toSave.setRevoked(partial.getRevoked());
        }

        return userTokenRepository.save(toSave);
    }

}
