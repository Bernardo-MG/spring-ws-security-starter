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

package com.bernardomg.security.usecase.login.service;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.domain.login.event.LogInEvent;
import com.bernardomg.security.domain.login.exception.InvalidCredentialsException;
import com.bernardomg.security.domain.login.model.Credentials;
import com.bernardomg.security.domain.login.model.TokenLoginStatus;
import com.bernardomg.security.usecase.login.authentication.LoginUserAuthenticator;
import com.bernardomg.security.usecase.login.domain.LoginUser;
import com.bernardomg.security.usecase.login.encoder.LoginTokenEncoder;

import jakarta.transaction.Transactional;

/**
 * Login service which generates a token for the logged in user.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Transactional
public final class TokenLoginService implements LoginService {

    /**
     * Logger for the class.
     */
    private static final Logger          log = LoggerFactory.getLogger(TokenLoginService.class);

    private final EventEmitter           eventEmitter;

    private final LoginTokenEncoder      loginTokenEncoder;

    private final LoginUserAuthenticator userAuthenticator;

    public TokenLoginService(final LoginUserAuthenticator userAuthent, final LoginTokenEncoder loginTokenEnc,
            final EventEmitter emitter) {
        super();

        userAuthenticator = Objects.requireNonNull(userAuthent);
        loginTokenEncoder = Objects.requireNonNull(loginTokenEnc);
        eventEmitter = Objects.requireNonNull(emitter);
    }

    @Override
    public final TokenLoginStatus login(final Credentials credentials) {
        final LogInEvent event;
        final LoginUser  user;
        final String     token;
        TokenLoginStatus status;

        log.trace("Log in attempt for {}", credentials.username());

        try {
            user = userAuthenticator.authenticate(credentials);

            token = loginTokenEncoder.encode(user);

            status = new TokenLoginStatus(true, token);

            log.debug("Successful login for {}", credentials.username());
        } catch (final InvalidCredentialsException exception) {
            status = new TokenLoginStatus(false, "");

            log.debug("Failed login for {}", credentials.username());
        }

        log.debug("Log in for {} with status {}", credentials.username(), status);

        // FIXME: the event root should be an object
        // TODO: Set source
        event = new LogInEvent(null, credentials.username(), status.logged());
        eventEmitter.emit(event);

        log.trace("Finished log in attempt for {}", credentials.username());

        return status;
    }

}
