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

package com.bernardomg.security.authentication.user.test.domain.repository.unit;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.model.UserChange;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.annotation.ValidUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserChanges;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authentication.user.test.config.factory.UserEntities;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("Role service - save change")
class ITUserRepositorySaveChange {

    @Autowired
    private UserRepository       repository;

    @Autowired
    private UserSpringRepository userSpringRepository;

    public ITUserRepositorySaveChange() {
        super();
    }

    @Test
    @DisplayName("Updates persisted data, ignoring case")
    @ValidUser
    void testSave_Case_PersistedData() {
        final UserChange       user;
        final List<UserEntity> entities;

        // GIVEN
        user = UserChanges.emailChangeUpperCase();

        // WHEN
        repository.save(UserConstants.USERNAME, user);

        // THEN
        entities = userSpringRepository.findAll();

        Assertions.assertThat(entities)
            .containsExactly(UserEntities.emailChange());
    }

    @Test
    @DisplayName("Returns the updated data, ignoring case")
    @ValidUser
    void testSave_Case_ReturnedData() {
        final UserChange user;
        final User       result;

        // GIVEN
        user = UserChanges.emailChangeUpperCase();

        // WHEN
        result = repository.save(UserConstants.USERNAME, user);

        // THEN
        Assertions.assertThat(result)
            .isEqualTo(Users.emailChange());
    }

    @Test
    @DisplayName("Can disable a user when updating")
    @ValidUser
    void testSave_Disable_PersistedData() {
        final UserChange       user;
        final List<UserEntity> entities;

        // GIVEN
        user = UserChanges.disabled();

        // WHEN
        repository.save(UserConstants.USERNAME, user);

        // THEN
        entities = userSpringRepository.findAll();

        Assertions.assertThat(entities)
            .containsExactly(UserEntities.disabled());
    }

    @Test
    @DisplayName("Can expire a user's password when updating")
    @ValidUser
    void testSave_ExpiredPassword_PersistedData() {
        final UserChange       user;
        final List<UserEntity> entities;

        // GIVEN
        user = UserChanges.passwordExpired();

        // WHEN
        repository.save(UserConstants.USERNAME, user);

        // THEN
        entities = userSpringRepository.findAll();

        Assertions.assertThat(entities)
            .containsExactly(UserEntities.passwordExpired());
    }

    @Test
    @DisplayName("With a user having padding whitespaces in username, name and email, these whitespaces are removed")
    @ValidUser
    void testSave_Padded_PersistedData() {
        final UserChange       user;
        final List<UserEntity> entities;

        // GIVEN
        user = UserChanges.paddedWithWhitespaces();

        // WHEN
        repository.save(UserConstants.USERNAME, user);

        // THEN
        entities = userSpringRepository.findAll();

        Assertions.assertThat(entities)
            .containsExactly(UserEntities.emailChange());
    }

    @Test
    @DisplayName("Updates persisted data")
    @ValidUser
    void testSave_PersistedData() {
        final UserChange       user;
        final List<UserEntity> entities;

        // GIVEN
        user = UserChanges.emailChange();

        // WHEN
        repository.save(UserConstants.USERNAME, user);

        // THEN
        entities = userSpringRepository.findAll();

        Assertions.assertThat(entities)
            .containsExactly(UserEntities.emailChange());
    }

    @Test
    @DisplayName("Returns the updated data")
    @ValidUser
    void testSave_ReturnedData() {
        final UserChange user;
        final User       result;

        // GIVEN
        user = UserChanges.emailChange();

        // WHEN
        result = repository.save(UserConstants.USERNAME, user);

        // THEN
        Assertions.assertThat(result)
            .isEqualTo(Users.emailChange());
    }

}
