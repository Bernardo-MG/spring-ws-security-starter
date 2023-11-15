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
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bernardomg.security.authentication.user.persistence.model.UserEntity;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.login.model.ImmutableLoginStatus;
import com.bernardomg.security.login.model.ImmutableTokenLoginStatus;
import com.bernardomg.security.login.model.LoginStatus;
import com.bernardomg.security.login.model.request.Login;
import com.bernardomg.security.login.model.request.LoginRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * Default implemention of the login service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class DefaultLoginService implements LoginService {

    private final Pattern           emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    private final Predicate<Login>  isValid;

    private final LoginTokenEncoder loginTokenEncoder;

    private final UserRepository    userRepository;

    public DefaultLoginService(final Predicate<Login> valid, final UserRepository userRepo,
            final LoginTokenEncoder loginTokenEnc) {
        super();

        isValid = Objects.requireNonNull(valid);
        userRepository = Objects.requireNonNull(userRepo);
        loginTokenEncoder = Objects.requireNonNull(loginTokenEnc);
    }

    @Override
    public final LoginStatus login(final Login login) {
        final Boolean valid;
        final String  username;
        final String  validUsername;
        final Login   loginWithName;

        username = login.getUsername()
            .toLowerCase();

        log.debug("Log in attempt for {}", username);

        loginWithName = loadLoginName(login);

        valid = isValid.test(loginWithName);

        validUsername = loginWithName.getUsername()
            .toLowerCase();
        return buildStatus(validUsername, valid);
    }

    private final LoginStatus buildStatus(final String username, final boolean logged) {
        final LoginStatus status;
        final String      token;

        if (logged) {
            token = loginTokenEncoder.encode(username);
            status = ImmutableTokenLoginStatus.builder()
                .username(username)
                .logged(logged)
                .token(token)
                .build();
        } else {
            status = ImmutableLoginStatus.builder()
                .username(username)
                .logged(logged)
                .build();
        }

        return status;
    }

    private final Login loadLoginName(final Login login) {
        final Matcher              emailMatcher;
        final Optional<UserEntity> readUser;
        final Login                validLogin;
        final String               username;

        username = login.getUsername()
            .toLowerCase();

        emailMatcher = emailPattern.matcher(username);

        if (emailMatcher.find()) {
            // Using email for login
            log.debug("Login attempt with email");
            // TODO: To lower case
            readUser = userRepository.findOneByEmail(username);
            if (readUser.isPresent()) {
                // Get the actual username and continue
                validLogin = LoginRequest.builder()
                    .username(readUser.get()
                        .getUsername())
                    .password(login.getPassword())
                    .build();
            } else {
                log.debug("No user found for email {}", username);
                validLogin = login;
            }
        } else {
            // Using username for login
            log.debug("Login attempt with username");
            validLogin = login;
        }

        return validLogin;
    }

}
