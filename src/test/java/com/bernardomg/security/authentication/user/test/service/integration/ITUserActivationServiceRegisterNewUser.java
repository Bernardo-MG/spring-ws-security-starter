
package com.bernardomg.security.authentication.user.test.service.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.persistence.model.UserEntity;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.service.UserActivationService;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
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
        final List<UserEntity> entities;

        service.registerNewUser(Users.USERNAME.toUpperCase(), Users.NAME, Users.EMAIL.toUpperCase());

        entities = repository.findAll();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(entities)
                .first()
                .extracting(UserEntity::getUsername)
                .isEqualTo(Users.USERNAME);
            softly.assertThat(entities)
                .first()
                .extracting(UserEntity::getEmail)
                .isEqualTo(Users.EMAIL);
        });
    }

    @Test
    @DisplayName("Returns the created data, ignoring case")
    void testRegisterNewUser_Case_ReturnedData() {
        final User result;

        result = service.registerNewUser(Users.USERNAME.toUpperCase(), Users.NAME, Users.EMAIL.toUpperCase());

        Assertions.assertThat(result)
            .isEqualTo(Users.newlyCreated());
    }

    @Test
    @DisplayName("With a user having padding whitespaces in username, name and email, these whitespaces are removed")
    void testRegisterNewUser_Padded_PersistedData() {
        final List<UserEntity> entities;

        service.registerNewUser(" " + Users.USERNAME + " ", " " + Users.NAME + " ", " " + Users.EMAIL + " ");

        entities = repository.findAll();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(entities)
                .first()
                .extracting(UserEntity::getUsername)
                .isEqualTo(Users.USERNAME);
            softly.assertThat(entities)
                .first()
                .extracting(UserEntity::getEmail)
                .isEqualTo(Users.EMAIL);
        });
    }

    @Test
    @DisplayName("Persists the data")
    void testRegisterNewUser_PersistedData() {
        final List<UserEntity> entities;

        service.registerNewUser(Users.USERNAME, Users.NAME, Users.EMAIL);

        entities = repository.findAll();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(entities)
                .first()
                .extracting(UserEntity::getUsername)
                .isEqualTo(Users.USERNAME);
            softly.assertThat(entities)
                .first()
                .extracting(UserEntity::getEmail)
                .isEqualTo(Users.EMAIL);
        });
    }

    @Test
    @DisplayName("Returns the created data")
    void testRegisterNewUser_ReturnedData() {
        final User result;

        result = service.registerNewUser(Users.USERNAME, Users.NAME, Users.EMAIL);

        Assertions.assertThat(result)
            .isEqualTo(Users.newlyCreated());
    }

}
