
package com.bernardomg.security.authentication.user.service.test.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.model.ImmutableUser;
import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.model.query.UserRegister;
import com.bernardomg.security.authentication.user.persistence.model.PersistentUser;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.service.UserActivationService;
import com.bernardomg.security.authentication.user.test.util.assertion.UserAssertions;
import com.bernardomg.security.authentication.user.test.util.model.UserRegisterRequests;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - register new user")
class ITUserActivationServiceRegisterNewUser {

    @Autowired
    private UserRepository        repository;

    @Autowired
    private UserActivationService service;

    public ITUserActivationServiceRegisterNewUser() {
        super();
    }

    @Test
    @DisplayName("Adds an entity when creating")
    void testRegisterNewUser_AddsEntity() {
        final UserRegister user;

        user = UserRegisterRequests.valid();

        service.registerNewUser(user);

        Assertions.assertThat(repository.count())
            .isEqualTo(1);
    }

    @Test
    @DisplayName("Persists the data, ignoring case")
    void testRegisterNewUser_Case_PersistedData() {
        final UserRegister   user;
        final PersistentUser entity;

        user = UserRegisterRequests.valid("ADMIN", "MAIL@SOMEWHERE.COM");

        service.registerNewUser(user);
        entity = repository.findAll()
            .iterator()
            .next();

        Assertions.assertThat(entity.getUsername())
            .isEqualTo(Users.USERNAME);
        Assertions.assertThat(entity.getEmail())
            .isEqualTo(Users.EMAIL);
    }

    @Test
    @DisplayName("Returns the created data, ignoring case")
    void testRegisterNewUser_Case_ReturnedData() {
        final UserRegister user;
        final User         result;

        user = UserRegisterRequests.valid("ADMIN", "MAIL@SOMEWHERE.COM");

        result = service.registerNewUser(user);

        Assertions.assertThat(result.getUsername())
            .isEqualTo(Users.USERNAME);
        Assertions.assertThat(result.getEmail())
            .isEqualTo(Users.EMAIL);
    }

    @Test
    @DisplayName("With a user having padding whitespaces in username, name and email, these whitespaces are removed")
    void testRegisterNewUser_Padded_PersistedData() {
        final UserRegister   user;
        final PersistentUser entity;

        user = UserRegisterRequests.paddedWithWhitespaces();

        service.registerNewUser(user);
        entity = repository.findAll()
            .iterator()
            .next();

        UserAssertions.isEqualTo(entity, PersistentUser.builder()
            .username(Users.USERNAME)
            .name(Users.NAME)
            .email(Users.EMAIL)
            .password("")
            .passwordExpired(true)
            .enabled(false)
            .expired(false)
            .locked(false)
            .build());
    }

    @Test
    @DisplayName("Persists the data")
    void testRegisterNewUser_PersistedData() {
        final UserRegister   user;
        final PersistentUser entity;

        user = UserRegisterRequests.valid();

        service.registerNewUser(user);
        entity = repository.findAll()
            .iterator()
            .next();

        UserAssertions.isEqualTo(entity, PersistentUser.builder()
            .username(Users.USERNAME)
            .name(Users.NAME)
            .email(Users.EMAIL)
            .password("")
            .passwordExpired(true)
            .enabled(false)
            .expired(false)
            .locked(false)
            .build());
    }

    @Test
    @DisplayName("Returns the created data")
    void testRegisterNewUser_ReturnedData() {
        final UserRegister user;
        final User         result;

        user = UserRegisterRequests.valid();

        result = service.registerNewUser(user);

        UserAssertions.isEqualTo(result, ImmutableUser.builder()
            .username(Users.USERNAME)
            .name(Users.NAME)
            .email(Users.EMAIL)
            .passwordExpired(true)
            .enabled(false)
            .expired(false)
            .locked(false)
            .build());
    }

}
