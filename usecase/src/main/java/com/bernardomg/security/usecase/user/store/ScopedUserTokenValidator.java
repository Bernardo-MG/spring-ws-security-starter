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

package com.bernardomg.security.usecase.user.store;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bernardomg.security.domain.user.exception.MissingUserTokenException;
import com.bernardomg.security.domain.user.model.UserToken;
import com.bernardomg.security.domain.user.repository.UserTokenRepository;

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
public final class ScopedUserTokenValidator implements TokenValidator {

    /**
     * Logger for the class.
     */
    private static final Logger       log = LoggerFactory.getLogger(ScopedUserTokenValidator.class);

    /**
     * Token scope.
     */
    private final String              tokenScope;

    /**
     * User tokens repository.
     */
    private final UserTokenRepository userTokenRepository;

    public ScopedUserTokenValidator(final UserTokenRepository tokenRepo, final String scope) {
        super();

        userTokenRepository = Objects.requireNonNull(tokenRepo);
        // TODO: maybe the scope should be received in the method
        tokenScope = Objects.requireNonNull(scope);
    }

    @Override
    public final void validate(final String token) {
        final UserToken read;

        log.trace("Validating token with scope {}", tokenScope);

        read = userTokenRepository.findOne(token)
            .orElseThrow(() -> {
                log.warn("Token not registered");
                throw new MissingUserTokenException(token);
            });

        read.checkStatus(tokenScope);
    }

}
