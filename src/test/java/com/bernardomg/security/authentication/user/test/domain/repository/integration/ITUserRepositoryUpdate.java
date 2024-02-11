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

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.annotation.ValidUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserEntities;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("Role service - update")
class ITUserRepositoryUpdate {

    @Autowired
    private UserRepository       repository;

    @Autowired
    private UserSpringRepository userSpringRepository;

    public ITUserRepositoryUpdate() {
        super();
    }

    @Test
    @DisplayName("When a user exists it is updated")
    @ValidUser
    void testUpdate_Existing_PersistedData() {
        final User             user;
        final List<UserEntity> entities;

        // GIVEN
        user = Users.enabled();

        // WHEN
        repository.update(user);

        // THEN
        entities = userSpringRepository.findAll();
        Assertions.assertThat(entities)
            .as("users")
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "password")
            .containsExactly(UserEntities.enabled());
    }

    @Test
    @DisplayName("When a user exists it is returned")
    @ValidUser
    void testUpdate_Existing_Returned() {
        final User user;
        final User created;

        // GIVEN
        user = Users.enabled();

        // WHEN
        created = repository.update(user);

        // THEN
        Assertions.assertThat(created)
            .as("user")
            .isEqualTo(Users.enabled());
    }

    @Test
    @DisplayName("Doesn't persist a not existing user")
    void testUpdate_NotExisting_PersistedData() {
        final User             user;
        final List<UserEntity> entities;

        // GIVEN
        user = Users.enabled();

        // WHEN
        repository.update(user);

        // THEN
        entities = userSpringRepository.findAll();
        Assertions.assertThat(entities)
            .as("users")
            .isEmpty();
    }

    @Test
    @DisplayName("Doesn't return a not existing user")
    void testUpdate_Returned() {
        final User user;
        final User created;

        // GIVEN
        user = Users.enabled();

        // WHEN
        created = repository.update(user);

        // THEN
        Assertions.assertThat(created)
            .as("user")
            .isNull();
    }

}
