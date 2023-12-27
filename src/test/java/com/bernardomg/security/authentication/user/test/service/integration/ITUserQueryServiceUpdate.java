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

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.exception.MissingUserUsernameException;
import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.model.UserChange;
import com.bernardomg.security.authentication.user.persistence.model.UserEntity;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.service.UserQueryService;
import com.bernardomg.security.authentication.user.test.config.annotation.ValidUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserChanges;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authentication.user.test.config.factory.UserEntities;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("Role service - update")
class ITUserQueryServiceUpdate {

    @Autowired
    private UserRepository   repository;

    @Autowired
    private UserQueryService service;

    public ITUserQueryServiceUpdate() {
        super();
    }

    @Test
    @DisplayName("Updates persisted data, ignoring case")
    @ValidUser
    void testUpdate_Case_PersistedData() {
        final UserChange       user;
        final List<UserEntity> entities;

        user = UserChanges.emailChangeUpperCase();

        service.update(UserConstants.USERNAME, user);
        entities = repository.findAll();

        Assertions.assertThat(entities)
            .containsExactly(UserEntities.emailChange());
    }

    @Test
    @DisplayName("Returns the updated data, ignoring case")
    @ValidUser
    void testUpdate_Case_ReturnedData() {
        final UserChange user;
        final User       result;

        user = UserChanges.emailChangeUpperCase();

        result = service.update(UserConstants.USERNAME, user);

        Assertions.assertThat(result)
            .isEqualTo(Users.emailChange());
    }

    @Test
    @DisplayName("Can disable a user when updating")
    @ValidUser
    void testUpdate_Disable_PersistedData() {
        final UserChange       user;
        final List<UserEntity> entities;

        user = UserChanges.disabled();

        service.update(UserConstants.USERNAME, user);
        entities = repository.findAll();

        Assertions.assertThat(entities)
            .containsExactly(UserEntities.disabled());
    }

    @Test
    @DisplayName("Can expire a user's password when updating")
    @ValidUser
    void testUpdate_ExpiredPassword_PersistedData() {
        final UserChange       user;
        final List<UserEntity> entities;

        user = UserChanges.passwordExpired();

        service.update(UserConstants.USERNAME, user);
        entities = repository.findAll();

        Assertions.assertThat(entities)
            .containsExactly(UserEntities.passwordExpired());
    }

    @Test
    @DisplayName("With a not existing user, an exception is thrown")
    void testUpdate_NotExisting_Exception() {
        final UserChange       user;
        final ThrowingCallable execution;

        user = UserChanges.emailChange();

        execution = () -> service.update(UserConstants.USERNAME, user);

        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingUserUsernameException.class);
    }

    @Test
    @DisplayName("With a user having padding whitespaces in username, name and email, these whitespaces are removed")
    @ValidUser
    void testUpdate_Padded_PersistedData() {
        final UserChange       user;
        final List<UserEntity> entities;

        user = UserChanges.paddedWithWhitespaces();

        service.update(UserConstants.USERNAME, user);
        entities = repository.findAll();

        Assertions.assertThat(entities)
            .containsExactly(UserEntities.emailChange());
    }

    @Test
    @DisplayName("Updates persisted data")
    @ValidUser
    void testUpdate_PersistedData() {
        final UserChange       user;
        final List<UserEntity> entities;

        user = UserChanges.emailChange();

        service.update(UserConstants.USERNAME, user);
        entities = repository.findAll();

        Assertions.assertThat(entities)
            .containsExactly(UserEntities.emailChange());
    }

    @Test
    @DisplayName("Returns the updated data")
    @ValidUser
    void testUpdate_ReturnedData() {
        final UserChange user;
        final User       result;

        user = UserChanges.emailChange();

        result = service.update(UserConstants.USERNAME, user);

        Assertions.assertThat(result)
            .isEqualTo(Users.emailChange());
    }

}
