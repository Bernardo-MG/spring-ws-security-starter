
package com.bernardomg.security.adapter.inbound.jpa.repository.test.integration.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.pagination.domain.Page;
import com.bernardomg.pagination.domain.Pagination;
import com.bernardomg.pagination.domain.Sorting;
import com.bernardomg.security.adapter.test.config.annotation.IntegrationTest;
import com.bernardomg.security.adapter.test.config.token.annotation.ActiveToken;
import com.bernardomg.security.adapter.test.config.token.annotation.ConsumedToken;
import com.bernardomg.security.adapter.test.config.token.annotation.ExpiredToken;
import com.bernardomg.security.adapter.test.config.token.annotation.RevokedToken;
import com.bernardomg.security.adapter.test.config.user.annotation.EnabledUserWithoutRole;
import com.bernardomg.security.adapter.test.config.user.factory.UserTokens;
import com.bernardomg.security.domain.user.model.UserToken;
import com.bernardomg.security.domain.user.repository.UserTokenRepository;

@IntegrationTest
@DisplayName("UserTokenRepository - find all")
class ITUserTokenRepositoryFindAll {

    @Autowired
    private UserTokenRepository repository;

    @Test
    @DisplayName("When there is an active token, it is returned")
    @EnabledUserWithoutRole
    @ActiveToken
    void testFindAll_Active() {
        final Page<UserToken> tokens;
        final Pagination      pagination;
        final Sorting         sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        tokens = repository.findAll(pagination, sorting);

        // THEN
        Assertions.assertThat(tokens.content())
            .as("tokens")
            .containsExactly(UserTokens.valid());
    }

    @Test
    @DisplayName("When there is a consumed token, it is returned")
    @EnabledUserWithoutRole
    @ConsumedToken
    void testFindAll_Consumed() {
        final Page<UserToken> tokens;
        final Pagination      pagination;
        final Sorting         sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        tokens = repository.findAll(pagination, sorting);

        // THEN
        Assertions.assertThat(tokens.content())
            .as("tokens")
            .containsExactly(UserTokens.consumed());
    }

    @Test
    @DisplayName("When there is an expired token, it is returned")
    @EnabledUserWithoutRole
    @ExpiredToken
    void testFindAll_Expired() {
        final Page<UserToken> tokens;
        final Pagination      pagination;
        final Sorting         sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        tokens = repository.findAll(pagination, sorting);

        // THEN
        Assertions.assertThat(tokens.content())
            .as("tokens")
            .containsExactly(UserTokens.expired());
    }

    @Test
    @DisplayName("When there is no data, nothing is returned")
    void testFindAll_NoData() {
        final Page<UserToken> tokens;
        final Pagination      pagination;
        final Sorting         sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        tokens = repository.findAll(pagination, sorting);

        // THEN
        Assertions.assertThat(tokens.content())
            .as("tokens")
            .isEmpty();
    }

    @Test
    @DisplayName("When there is a revoked token, it is returned")
    @EnabledUserWithoutRole
    @RevokedToken
    void testFindAll_Revoked() {
        final Page<UserToken> tokens;
        final Pagination      pagination;
        final Sorting         sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        tokens = repository.findAll(pagination, sorting);

        // THEN
        Assertions.assertThat(tokens.content())
            .as("tokens")
            .containsExactly(UserTokens.revoked());
    }

}
