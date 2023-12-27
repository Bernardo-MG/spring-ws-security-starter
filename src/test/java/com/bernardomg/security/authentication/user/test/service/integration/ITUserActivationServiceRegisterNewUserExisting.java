
package com.bernardomg.security.authentication.user.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.service.UserActivationService;
import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("User service - register new user - existing")
class ITUserActivationServiceRegisterNewUserExisting {

    @Autowired
    private UserRepository        repository;

    @Autowired
    private UserActivationService service;

    public ITUserActivationServiceRegisterNewUserExisting() {
        super();
    }

    @Test
    @DisplayName("Adds an entity when creating with an existing id")
    @OnlyUser
    void testRegisterNewUser_AddsEntity() {
        service.registerNewUser(UserConstants.ALTERNATIVE_USERNAME, UserConstants.NAME,
            UserConstants.ALTERNATIVE_EMAIL);

        Assertions.assertThat(repository.count())
            .isEqualTo(2);
    }

}
