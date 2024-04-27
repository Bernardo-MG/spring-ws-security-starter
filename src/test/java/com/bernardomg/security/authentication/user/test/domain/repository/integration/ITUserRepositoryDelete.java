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
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.annotation.ValidUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleSpringRepository;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User repository - delete without roles")
class ITUserRepositoryDelete {

    @Autowired
    private RoleSpringRepository roleSpringRepository;

    @Autowired
    private UserRepository       userRepository;

    @Autowired
    private UserSpringRepository userSpringRepository;

    public ITUserRepositoryDelete() {
        super();
    }

    @Test
    @DisplayName("Removes an entity when deleting")
    @OnlyUser
    void testDelete() {
        // WHEN
        userRepository.delete(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(userSpringRepository.count())
            .isZero();
    }

    @Test
    @DisplayName("Does not remove roles when deleting")
    @ValidUser
    void testDelete_DoesNotRemoveRelations() {
        // WHEN
        userRepository.delete(UserConstants.USERNAME);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(userSpringRepository.count())
                .isZero();
            softly.assertThat(roleSpringRepository.count())
                .isEqualTo(1);
        });
    }

    @Test
    @DisplayName("When there is no data, nothing is removed")
    void testDelete_NoData() {
        // WHEN
        userRepository.delete(UserConstants.USERNAME);

        // THEN
        Assertions.assertThat(userSpringRepository.count())
            .isZero();
    }

}
