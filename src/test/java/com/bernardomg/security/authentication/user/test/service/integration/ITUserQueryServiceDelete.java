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

package com.bernardomg.security.authentication.user.test.service.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authentication.user.domain.exception.MissingUserUsernameException;
import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.annotation.ValidUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authentication.user.usecase.service.UserQueryService;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleSpringRepository;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User service - delete without roles")
class ITUserQueryServiceDelete {

    @Autowired
    private UserSpringRepository repository;

    @Autowired
    private RoleSpringRepository       roleRepository;

    @Autowired
    private UserQueryService     service;

    public ITUserQueryServiceDelete() {
        super();
    }

    @Test
    @DisplayName("Does not remove roles when deleting")
    @ValidUser
    void testDelete_DoesNotRemoveRelations() {
        service.delete(UserConstants.USERNAME);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(repository.count())
                .isZero();
            softly.assertThat(roleRepository.count())
                .isEqualTo(1);
        });
    }

    @Test
    @DisplayName("With a not existing user, an exception is thrown")
    void testDelete_NotExisting() {
        final ThrowingCallable execution;

        execution = () -> service.delete(UserConstants.USERNAME);

        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingUserUsernameException.class);
    }

    @Test
    @DisplayName("Removes an entity when deleting")
    @OnlyUser
    void testDelete_RemovesEntity() {
        service.delete(UserConstants.USERNAME);

        Assertions.assertThat(repository.count())
            .isZero();
    }

}
