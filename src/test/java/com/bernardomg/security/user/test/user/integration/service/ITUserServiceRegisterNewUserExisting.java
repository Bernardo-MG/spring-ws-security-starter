
package com.bernardomg.security.user.test.user.integration.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.user.model.User;
import com.bernardomg.security.user.model.request.UserCreate;
import com.bernardomg.security.user.persistence.repository.UserRepository;
import com.bernardomg.security.user.service.UserService;
import com.bernardomg.security.user.test.config.OnlyUser;
import com.bernardomg.security.user.test.util.model.UsersCreate;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - register new user - existing")
class ITUserServiceRegisterNewUserExisting {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService    service;

    public ITUserServiceRegisterNewUserExisting() {
        super();
    }

    @Test
    @DisplayName("Doesn't create over existing ids")
    @OnlyUser
    void testRegisterNewUser() {
        final User       result;
        final UserCreate user;

        user = UsersCreate.alternative();

        result = service.registerNewUser(user);

        // TODO: What is this tests for?
        Assertions.assertThat(result.getId())
            .isNotEqualTo(1);
    }

    @Test
    @DisplayName("Adds an entity when creating with an existing id")
    @OnlyUser
    void testRegisterNewUser_AddsEntity() {
        final UserCreate user;

        user = UsersCreate.alternative();

        service.registerNewUser(user);

        Assertions.assertThat(repository.count())
            .isEqualTo(2);
    }

}
