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

package com.bernardomg.security.user.test.user.integration.service;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.exception.InvalidIdException;
import com.bernardomg.security.permission.persistence.repository.ActionRepository;
import com.bernardomg.security.user.persistence.repository.RoleRepository;
import com.bernardomg.security.user.persistence.repository.UserRepository;
import com.bernardomg.security.user.service.UserService;
import com.bernardomg.security.user.test.config.OnlyUser;
import com.bernardomg.security.user.test.config.ValidUser;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - delete without roles")
class ITUserServiceDelete {

    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private UserRepository   repository;

    @Autowired
    private RoleRepository   roleRepository;

    @Autowired
    private UserService      service;

    public ITUserServiceDelete() {
        super();
    }

    @Test
    @DisplayName("Does not remove roles or action when deleting")
    @ValidUser
    void testDelete_DoesNotRemoveRelations() {
        service.delete(1L);

        Assertions.assertThat(repository.count())
            .isZero();
        Assertions.assertThat(roleRepository.count())
            .isEqualTo(1);
        Assertions.assertThat(actionRepository.count())
            .isEqualTo(4);
    }

    @Test
    @DisplayName("With a not existing id, an exception is thrown")
    void testDelete_NotExisting() {
        final ThrowingCallable execution;

        execution = () -> service.delete(1L);

        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(InvalidIdException.class);
    }

    @Test
    @DisplayName("Removes an entity when deleting")
    @OnlyUser
    void testDelete_RemovesEntity() {
        service.delete(1L);

        Assertions.assertThat(repository.count())
            .isZero();
    }

}
