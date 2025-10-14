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

package com.bernardomg.security.login.usecase.service;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.event.LogInEvent;
import com.bernardomg.security.login.domain.model.Credentials;
import com.bernardomg.security.login.domain.model.TokenLoginStatus;
import com.bernardomg.security.login.usecase.encoder.LoginTokenEncoder;
import com.bernardomg.security.user.domain.model.User;
import com.bernardomg.security.user.domain.repository.UserRepository;

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
    private static final Logger          log          = LoggerFactory.getLogger(TokenLoginService.class);

    private final Pattern                emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    private final EventEmitter           eventEmitter;

    private final Predicate<Credentials> isValid;

    private final LoginTokenEncoder      loginTokenEncoder;

    private final UserRepository         userRepository;

    public TokenLoginService(final Predicate<Credentials> valid, final UserRepository userRepo,
            final LoginTokenEncoder loginTokenEnc, final EventEmitter emitter) {
        super();

        isValid = Objects.requireNonNull(valid);
        userRepository = Objects.requireNonNull(userRepo);
        loginTokenEncoder = Objects.requireNonNull(loginTokenEnc);
        eventEmitter = Objects.requireNonNull(emitter);
    }

    @Override
    public final TokenLoginStatus login(final Credentials credentials) {
        final Boolean          valid;
        final String           validUsername;
        final TokenLoginStatus status;
        final LogInEvent       event;
        final Credentials      validCredentials;

        log.trace("Log in attempt for {}", credentials.username());

        validUsername = loadLoginName(credentials.username()
            .trim());

        validCredentials = new Credentials(validUsername, credentials.password()
            .trim());
        valid = isValid.test(validCredentials);

        status = buildStatus(validUsername, valid);

        log.debug("Log in for {} with status {}", credentials.username(), status);

        // FIXME: the event root should be an object
        event = new LogInEvent(this, validUsername, valid);
        eventEmitter.emit(event);

        log.trace("Finished log in attempt for {}", credentials.username());

        return status;
    }

    private final TokenLoginStatus buildStatus(final String username, final boolean logged) {
        final TokenLoginStatus status;
        final String           token;

        if (logged) {
            token = loginTokenEncoder.encode(username);
            status = new TokenLoginStatus(logged, token);
        } else {
            status = new TokenLoginStatus(logged, "");
        }

        return status;
    }

    private final String loadLoginName(final String username) {
        final Matcher        emailMatcher;
        final Optional<User> readUser;
        final String         validUsername;

        emailMatcher = emailPattern.matcher(username);

        if (emailMatcher.find()) {
            // Using email for login
            log.debug("Login attempt with email");
            // TODO: To lower case
            readUser = userRepository.findOneByEmail(username);
            if (readUser.isPresent()) {
                // Get the actual username and continue
                validUsername = readUser.get()
                    .username();
            } else {
                log.debug("No user found for email {}", username);
                validUsername = username.toLowerCase(LocaleContextHolder.getLocale());
            }
        } else {
            // Using username for login
            log.debug("Login attempt with username");
            validUsername = username.toLowerCase(LocaleContextHolder.getLocale());
        }

        return validUsername;
    }

}
