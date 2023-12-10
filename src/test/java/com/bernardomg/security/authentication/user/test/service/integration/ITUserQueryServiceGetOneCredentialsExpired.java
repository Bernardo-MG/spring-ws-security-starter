
package com.bernardomg.security.authentication.user.test.service.integration;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.bernardomg.security.authentication.user.model.User;
import com.bernardomg.security.authentication.user.service.UserQueryService;
import com.bernardomg.security.authentication.user.test.util.assertion.UserAssertions;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("User service - get one - password expired")
@Sql({ "/db/queries/security/user/password_expired.sql" })
class ITUserServiceGetOnepasswordExpired {

    @Autowired
    private UserQueryService service;

    public ITUserServiceGetOnepasswordExpired() {
        super();
    }

    @Test
    @DisplayName("Returns a single entity by id")
    void testGetOne_Existing() {
        final Optional<User> result;

        result = service.getOne(1l);

        Assertions.assertThat(result)
            .isPresent();
    }

    @Test
    @DisplayName("Returns the correct data when reading a single entity")
    void testGetOne_Existing_Data() {
        final User result;

        result = service.getOne(1l)
            .get();

        UserAssertions.isEqualTo(result, Users.passwordExpired());
    }

}