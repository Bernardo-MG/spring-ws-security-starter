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

package com.bernardomg.security.user.data.usecase.service;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.password.validation.PasswordResetHasStrongPasswordRule;
import com.bernardomg.security.user.data.domain.exception.InvalidTokenException;
import com.bernardomg.security.user.data.domain.exception.MissingUsernameException;
import com.bernardomg.security.user.data.domain.model.User;
import com.bernardomg.security.user.data.domain.model.UserTokenStatus;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.data.usecase.store.UserTokenStore;
import com.bernardomg.validation.validator.FieldRuleValidator;
import com.bernardomg.validation.validator.Validator;

/**
 * Default user activation service.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Transactional
public final class DefaultUserActivationService implements UserActivationService {

    /**
     * Logger for the class.
     */
    private static final Logger     log = LoggerFactory.getLogger(DefaultUserActivationService.class);

    /**
     * Token processor.
     */
    private final UserTokenStore    tokenStore;

    /**
     * User repository.
     */
    private final UserRepository    userRepository;

    /**
     * User activation validator.
     */
    private final Validator<String> validatorActivate;

    public DefaultUserActivationService(final UserRepository userRepo, final UserTokenStore tStore) {
        super();

        userRepository = Objects.requireNonNull(userRepo);
        tokenStore = Objects.requireNonNull(tStore);

        validatorActivate = new FieldRuleValidator<>(new PasswordResetHasStrongPasswordRule());
    }

    @Override
    public final User activateUser(final String token, final String password) {
        final String username;
        final User   user;
        final User   saved;

        log.trace("Activating new user");

        // Validate token
        tokenStore.validate(token);

        // Validate password
        validatorActivate.validate(password.trim());

        // Acquire username
        username = tokenStore.getUsername(token);

        log.debug("Activating new user {}", username);

        user = userRepository.findOne(username)
            .orElseThrow(() -> {
                log.error("Missing user {}", username);
                throw new MissingUsernameException(username);
            });

        // TODO: validate somehow that it is actually a new user
        user.checkStatus();

        saved = userRepository.activate(username, password.trim());
        tokenStore.consumeToken(token);

        log.trace("Activated new user {}", username);

        return saved;
    }

    @Override
    public final UserTokenStatus validateToken(final String token) {
        final UserTokenStatus status;
        final String          username;
        boolean               valid;

        log.trace("Validating user activation token");

        try {
            // TODO: maybe return a boolean instead of throwing an exception
            tokenStore.validate(token);
            valid = true;
        } catch (final InvalidTokenException ex) {
            valid = false;
        }
        username = tokenStore.getUsername(token);

        status = new UserTokenStatus(username, valid);

        log.trace("Validated user activation token with status {}", status);

        return status;
    }

}
