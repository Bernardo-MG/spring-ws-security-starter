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

import com.bernardomg.security.authentication.user.exception.MissingUserException;
import com.bernardomg.security.authentication.user.model.DtoUser;
import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.model.query.UserUpdate;
import com.bernardomg.security.authentication.user.persistence.model.PersistentUser;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.service.UserService;
import com.bernardomg.security.user.test.config.ValidUser;
import com.bernardomg.security.user.test.util.assertion.UserAssertions;
import com.bernardomg.security.user.test.util.model.UsersUpdate;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("Role service - update")
class ITUserServiceUpdate {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService    service;

    public ITUserServiceUpdate() {
        super();
    }

    @Test
    @DisplayName("Adds no entity when updating")
    @ValidUser
    void testUpdate_AddsNoEntity() {
        final UserUpdate user;

        user = UsersUpdate.emailChange();

        service.update(1L, user);

        Assertions.assertThat(repository.count())
            .isEqualTo(1);
    }

    @Test
    @DisplayName("Updates persisted data, ignoring case")
    @ValidUser
    void testUpdate_Case_PersistedData() {
        final UserUpdate     user;
        final PersistentUser entity;

        user = UsersUpdate.emailChangeUpperCase();

        service.update(1L, user);
        entity = repository.findAll()
            .iterator()
            .next();

        Assertions.assertThat(entity.getEmail())
            .isEqualTo("email2@somewhere.com");
    }

    @Test
    @DisplayName("Returns the updated data, ignoring case")
    @ValidUser
    void testUpdate_Case_ReturnedData() {
        final UserUpdate user;
        final User       result;

        user = UsersUpdate.emailChangeUpperCase();

        result = service.update(1L, user);

        Assertions.assertThat(result.getEmail())
            .isEqualTo("email2@somewhere.com");
    }

    @Test
    @DisplayName("Can disable a user when updating")
    @ValidUser
    void testUpdate_Disable_PersistedData() {
        final UserUpdate     user;
        final PersistentUser entity;

        user = UsersUpdate.disabled();

        service.update(1L, user);
        entity = repository.findAll()
            .iterator()
            .next();

        UserAssertions.isEqualTo(entity, PersistentUser.builder()
            .username("admin")
            .name("Admin")
            .email("email@somewhere.com")
            .password("$2a$04$gV.k/KKIqr3oPySzs..bx.8absYRTpNe8AbHmPP90.ErW0ICGOsVW")
            .passwordExpired(false)
            .enabled(false)
            .expired(false)
            .locked(false)
            .build());
    }

    @Test
    @DisplayName("Can expire a user's password when updating")
    @ValidUser
    void testUpdate_ExpiredPassword_PersistedData() {
        final UserUpdate     user;
        final PersistentUser entity;

        user = UsersUpdate.passwordExpired();

        service.update(1L, user);
        entity = repository.findAll()
            .iterator()
            .next();

        UserAssertions.isEqualTo(entity, PersistentUser.builder()
            .username("admin")
            .name("Admin")
            .email("email@somewhere.com")
            .password("$2a$04$gV.k/KKIqr3oPySzs..bx.8absYRTpNe8AbHmPP90.ErW0ICGOsVW")
            .passwordExpired(true)
            .enabled(true)
            .expired(false)
            .locked(false)
            .build());
    }

    @Test
    @DisplayName("With a not existing entity, an exception is thrown")
    void testUpdate_NotExisting_Exception() {
        final UserUpdate       user;
        final ThrowingCallable execution;

        user = UsersUpdate.emailChange();

        execution = () -> service.update(1L, user);

        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingUserException.class);
    }

    @Test
    @DisplayName("With a user having padding whitespaces in username, name and email, these whitespaces are removed")
    @ValidUser
    void testUpdate_Padded_PersistedData() {
        final UserUpdate     user;
        final PersistentUser entity;

        user = UsersUpdate.paddedWithWhitespaces();

        service.update(1L, user);
        entity = repository.findAll()
            .iterator()
            .next();

        Assertions.assertThat(entity.getEmail())
            .isEqualTo("email2@somewhere.com");
    }

    @Test
    @DisplayName("Updates persisted data")
    @ValidUser
    void testUpdate_PersistedData() {
        final UserUpdate     user;
        final PersistentUser entity;

        user = UsersUpdate.emailChange();

        service.update(1L, user);
        entity = repository.findAll()
            .iterator()
            .next();

        UserAssertions.isEqualTo(entity, PersistentUser.builder()
            .username("admin")
            .name("Admin")
            .email("email2@somewhere.com")
            .password("$2a$04$gV.k/KKIqr3oPySzs..bx.8absYRTpNe8AbHmPP90.ErW0ICGOsVW")
            .passwordExpired(false)
            .enabled(true)
            .expired(false)
            .locked(false)
            .build());
    }

    @Test
    @DisplayName("Returns the updated data")
    @ValidUser
    void testUpdate_ReturnedData() {
        final UserUpdate user;
        final User       result;

        user = UsersUpdate.emailChange();

        result = service.update(1L, user);

        UserAssertions.isEqualTo(result, DtoUser.builder()
            .username("admin")
            .name("Admin")
            .email("email2@somewhere.com")
            .passwordExpired(false)
            .enabled(true)
            .expired(false)
            .locked(false)
            .build());
    }

}
