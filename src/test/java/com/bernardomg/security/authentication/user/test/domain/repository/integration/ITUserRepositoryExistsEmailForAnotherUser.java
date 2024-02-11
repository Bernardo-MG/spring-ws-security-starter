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

package com.bernardomg.security.authentication.user.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.annotation.AlternativeUser;
import com.bernardomg.security.authentication.user.test.config.annotation.ValidUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserRepository - exists email for another user")
class ITUserRepositoryExistsEmailForAnotherUser {

    @Autowired
    private UserRepository userRepository;

    public ITUserRepositoryExistsEmailForAnotherUser() {
        super();
    }

    @Test
    @DisplayName("When there is another user with the email it exists")
    @ValidUser
    @AlternativeUser
    void testExistsEmailForAnotherUser_AnotherUser() {
        final boolean exists;

        // WHEN
        exists = userRepository.existsEmailForAnotherUser(UserConstants.USERNAME, UserConstants.ALTERNATIVE_EMAIL);

        // THEN
        Assertions.assertThat(exists)
            .as("exists")
            .isTrue();
    }

    @Test
    @DisplayName("When there is no data the email doesn't exist")
    void testExistsEmailForAnotherUser_NoData() {
        final boolean exists;

        // WHEN
        exists = userRepository.existsEmailForAnotherUser(UserConstants.USERNAME, UserConstants.ALTERNATIVE_EMAIL);

        // THEN
        Assertions.assertThat(exists)
            .as("exists")
            .isFalse();
    }

    @Test
    @DisplayName("When the user exists and there is not another user the email doesn't exist")
    @ValidUser
    void testExistsEmailForAnotherUser_SingleUser() {
        final boolean exists;

        // WHEN
        exists = userRepository.existsEmailForAnotherUser(UserConstants.USERNAME, UserConstants.ALTERNATIVE_EMAIL);

        // THEN
        Assertions.assertThat(exists)
            .as("exists")
            .isFalse();
    }

}
