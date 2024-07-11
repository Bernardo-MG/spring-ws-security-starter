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

package com.bernardomg.security.authentication.user.usecase.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.authentication.user.domain.exception.MissingUserException;
import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Default implementation of the user service.
 */
@Slf4j
@Transactional
public final class DefaultUserAccessService implements UserAccessService {

    /**
     * Max login attempts. Once the user reaches this number, it is locked.
     */
    private final int            maxAttempts;

    /**
     * User repository.
     */
    private final UserRepository userRepository;

    public DefaultUserAccessService(final int maxAttmp, final UserRepository userRepo) {
        super();

        maxAttempts = Objects.requireNonNull(maxAttmp);
        userRepository = Objects.requireNonNull(userRepo);
    }

    @Override
    public final void checkForLocking(final String username) {
        final int            attempts;
        final Optional<User> read;
        final User           user;

        log.debug("Checking {} for locking user", username);

        // Get number of attempts
        attempts = userRepository.increaseLoginAttempts(username);

        log.debug("User {} had {} login attempts, out of a max of {}", username, attempts, maxAttempts);

        // If attempts reached the max
        if (attempts >= maxAttempts) {
            // Then the user is locked
            read = userRepository.findOne(username);
            if (read.isEmpty()) {
                log.error("Missing user {}", username);
                throw new MissingUserException(username);
            }
            user = read.get();
            userRepository.lock(user.getUsername());
            log.debug("Locked user {} after {} login attempts", username, attempts);
        } else {
            log.debug("User {} had {} login attempts out of a max of {}. Won't be locked", username, attempts,
                maxAttempts);
        }
    }

    @Override
    public final void clearLoginAttempts(final String username) {
        final int attempts;

        log.debug("Clearing login attempts for {}", username);

        attempts = userRepository.getLoginAttempts(username);
        if (attempts > 0) {
            log.debug("User {} had {} login attempts. Clearing them", username, attempts);
            userRepository.clearLoginAttempts(username);
        } else {
            log.debug("User {} had no login attempts. Nothing to clear", username);
        }
    }

}
