
package com.bernardomg.security.authentication.user.service.test.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.persistence.model.UserEntity;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.service.UserActivationService;
import com.bernardomg.security.authentication.user.test.util.assertion.UserAssertions;
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
        service.registerNewUser(Users.USERNAME, Users.NAME, Users.EMAIL);

        Assertions.assertThat(repository.count())
            .isEqualTo(1);
    }

    @Test
    @DisplayName("Persists the data, ignoring case")
    void testRegisterNewUser_Case_PersistedData() {
        final UserEntity entity;

        service.registerNewUser(Users.USERNAME.toUpperCase(), Users.NAME, Users.EMAIL.toUpperCase());
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
        final User result;

        result = service.registerNewUser(Users.USERNAME.toUpperCase(), Users.NAME, Users.EMAIL.toUpperCase());

        Assertions.assertThat(result.getUsername())
            .isEqualTo(Users.USERNAME);
        Assertions.assertThat(result.getEmail())
            .isEqualTo(Users.EMAIL);
    }

    @Test
    @DisplayName("With a user having padding whitespaces in username, name and email, these whitespaces are removed")
    void testRegisterNewUser_Padded_PersistedData() {
        final UserEntity entity;

        service.registerNewUser(" " + Users.USERNAME + " ", " " + Users.NAME + " ", " " + Users.EMAIL + " ");
        entity = repository.findAll()
            .iterator()
            .next();

        UserAssertions.isEqualTo(entity, UserEntity.builder()
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
        final UserEntity entity;

        service.registerNewUser(Users.USERNAME, Users.NAME, Users.EMAIL);

        Assertions.assertThat(repository.count())
            .isEqualTo(1);

        entity = repository.findAll()
            .iterator()
            .next();

        UserAssertions.isEqualTo(entity, UserEntity.builder()
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
        final User result;

        result = service.registerNewUser(Users.USERNAME, Users.NAME, Users.EMAIL);

        UserAssertions.isEqualTo(result, Users.newlyCreated());
    }

}
