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

package com.bernardomg.security.login.service;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.ApplicationEventPublisher;

import com.bernardomg.security.authentication.user.persistence.model.UserEntity;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.login.event.LogInEvent;
import com.bernardomg.security.login.model.TokenLoginStatus;

import lombok.extern.slf4j.Slf4j;

/**
 * Login service which generates a token for the logged in user.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class TokenLoginService implements LoginService {

    private final Pattern                     emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    private final ApplicationEventPublisher   eventPublisher;

    private final BiPredicate<String, String> isValid;

    private final LoginTokenEncoder           loginTokenEncoder;

    private final UserRepository              userRepository;

    public TokenLoginService(final BiPredicate<String, String> valid, final UserRepository userRepo,
            final LoginTokenEncoder loginTokenEnc, final ApplicationEventPublisher publisher) {
        super();

        isValid = Objects.requireNonNull(valid);
        userRepository = Objects.requireNonNull(userRepo);
        loginTokenEncoder = Objects.requireNonNull(loginTokenEnc);
        eventPublisher = Objects.requireNonNull(publisher);
    }

    @Override
    public final TokenLoginStatus login(final String username, final String password) {
        final Boolean          valid;
        final String           validUsername;
        final TokenLoginStatus status;
        final LogInEvent       event;

        log.debug("Log in attempt for {}", username);

        validUsername = loadLoginName(username).toLowerCase();

        valid = isValid.test(validUsername, password);

        status = buildStatus(validUsername, valid);

        event = new LogInEvent(this, validUsername, valid);
        eventPublisher.publishEvent(event);

        return status;
    }

    private final TokenLoginStatus buildStatus(final String username, final boolean logged) {
        final TokenLoginStatus status;
        final String           token;

        if (logged) {
            token = loginTokenEncoder.encode(username);
            status = TokenLoginStatus.builder()
                .withLogged(logged)
                .withToken(token)
                .build();
        } else {
            status = TokenLoginStatus.builder()
                .withLogged(logged)
                .withToken("")
                .build();
        }

        return status;
    }

    private final String loadLoginName(final String username) {
        final Matcher              emailMatcher;
        final Optional<UserEntity> readUser;
        final String               validUsername;

        emailMatcher = emailPattern.matcher(username);

        if (emailMatcher.find()) {
            // Using email for login
            log.debug("Login attempt with email");
            // TODO: To lower case
            readUser = userRepository.findOneByEmail(username);
            if (readUser.isPresent()) {
                // Get the actual username and continue
                validUsername = readUser.get()
                    .getUsername();
            } else {
                log.debug("No user found for email {}", username);
                validUsername = username.toLowerCase();
            }
        } else {
            // Using username for login
            log.debug("Login attempt with username");
            validUsername = username.toLowerCase();
        }

        return validUsername;
    }

}
