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

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.user.data.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.user.data.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.test.config.annotation.MaxLoginAttemptsUser;
import com.bernardomg.security.user.test.config.annotation.ValidUser;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User repository - increase login attempts")
class ITUserRepositoryIncreaseLoginAttempts {

    @Autowired
    private UserRepository       userRepository;

    @Autowired
    private UserSpringRepository userSpringRepository;

    public ITUserRepositoryIncreaseLoginAttempts() {
        super();
    }

    @Test
    @DisplayName("When the user has login attempts, these are persisted")
    @MaxLoginAttemptsUser
    void testLoginAttempts_MaxAttempts_PersistedData() {
        final List<UserEntity> users;

        // WHEN
        userRepository.increaseLoginAttempts(UserConstants.USERNAME);

        // THEN
        users = userSpringRepository.findAll();
        Assertions.assertThat(users)
            .as("users")
            .hasSize(1)
            .first()
            .extracting(UserEntity::getLoginAttempts)
            .as("login attempts")
            .isEqualTo(UserConstants.MAX_LOGIN_ATTEMPTS + 1);
    }

    @Test
    @DisplayName("When the user has login attempts, these are returned")
    @MaxLoginAttemptsUser
    void testLoginAttempts_MaxAttempts_ReturnedData() {
        final int attempts;

        // WHEN
        attempts = userRepository.increaseLoginAttempts(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(attempts)
            .as("attempts")
            .isEqualTo(UserConstants.MAX_LOGIN_ATTEMPTS + 1);
    }

    @Test
    @DisplayName("When the user has no login attempts, a single attempt is persisted")
    @ValidUser
    void testLoginAttempts_NoAttempts_PersistedData() {
        final List<UserEntity> users;

        // WHEN
        userRepository.increaseLoginAttempts(UserConstants.USERNAME);

        // THEN
        users = userSpringRepository.findAll();
        Assertions.assertThat(users)
            .as("users")
            .hasSize(1)
            .first()
            .extracting(UserEntity::getLoginAttempts)
            .as("login attempts")
            .isEqualTo(1);
    }

    @Test
    @DisplayName("When the user has no login attempts, a single attempt is returned")
    @ValidUser
    void testLoginAttempts_NoAttempts_ReturnedData() {
        final int attempts;

        // WHEN
        attempts = userRepository.increaseLoginAttempts(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(attempts)
            .as("attempts")
            .isEqualTo(1);
    }

    @Test
    @DisplayName("When there is no data, zero attempts are returned")
    void testLoginAttempts_NoData_ReturnedData() {
        final int attempts;

        // WHEN
        attempts = userRepository.increaseLoginAttempts(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(attempts)
            .as("attempts")
            .isEqualTo(-1);
    }

}
