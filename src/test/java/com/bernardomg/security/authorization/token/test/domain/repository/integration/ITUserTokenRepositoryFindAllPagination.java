
package com.bernardomg.security.authorization.token.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authorization.token.domain.model.UserToken;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokens;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserTokenRepository - find all - pagination")
@OnlyUser
@ValidUserToken
class ITUserTokenRepositoryFindAllPagination {

    @Autowired
    private UserTokenRepository repository;

    @Test
    @DisplayName("Returns a page")
    void testFindAll_Page_Container() {
        final Iterable<UserToken> logins;
        final Pageable            pageable;

        // GIVEN
        pageable = Pageable.ofSize(10);

        // WHEN
        logins = repository.findAll(pageable);

        // THEN
        Assertions.assertThat(logins)
            .as("logins")
            .isInstanceOf(Page.class);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testFindAll_Page1() {
        final Iterable<UserToken> logins;
        final Pageable            pageable;

        // GIVEN
        pageable = PageRequest.of(0, 1);

        // WHEN
        logins = repository.findAll(pageable);

        // THEN
        Assertions.assertThat(logins)
            .as("logins")
            .containsExactly(UserTokens.valid());
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testFindAll_Page2() {
        final Iterable<UserToken> logins;
        final Pageable            pageable;

        // GIVEN
        pageable = PageRequest.of(1, 1);

        // WHEN
        logins = repository.findAll(pageable);

        // THEN
        Assertions.assertThat(logins)
            .isEmpty();
    }

    @Test
    @DisplayName("Returns a page when the pagination is disabled")
    void testFindAll_Unpaged_Container() {
        final Iterable<UserToken> logins;
        final Pageable            pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        logins = repository.findAll(pageable);

        // THEN
        Assertions.assertThat(logins)
            .as("logins")
            .isInstanceOf(Page.class);
    }

}
