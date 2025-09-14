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

package com.bernardomg.security.user.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.user.domain.repository.UserRepository;
import com.bernardomg.security.user.test.config.annotation.MaxLoginAttemptsUser;
import com.bernardomg.security.user.test.config.annotation.ValidUser;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.test.config.factory.UserEntities;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User repository - clear login attempts")
class ITUserRepositoryClearLoginAttempts {

    @Autowired
    private UserRepository       userRepository;

    @Autowired
    private UserSpringRepository userSpringRepository;

    public ITUserRepositoryClearLoginAttempts() {
        super();
    }

    @Test
    @DisplayName("When the user has login attempts, these are removed")
    @MaxLoginAttemptsUser
    void testClearLoginAttempts_MaxAttempts() {

        // WHEN
        userRepository.clearLoginAttempts(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(userSpringRepository.findAll())
            .as("users")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactly(UserEntities.enabled());
    }

    @Test
    @DisplayName("When the user has no login attempts, these are removed")
    @ValidUser
    void testClearLoginAttempts_NoAttempts() {

        // WHEN
        userRepository.clearLoginAttempts(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(userSpringRepository.findAll())
            .as("users")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactly(UserEntities.enabled());
    }

    @Test
    @DisplayName("When there is no data, nothing is done")
    void testClearLoginAttempts_NoData() {

        // WHEN
        userRepository.clearLoginAttempts(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(userSpringRepository.findAll())
            .as("users")
            .isEmpty();
    }

}
