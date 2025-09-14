
package com.bernardomg.security.user.token.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.user.domain.model.UserToken;
import com.bernardomg.security.user.domain.repository.UserTokenRepository;
import com.bernardomg.security.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.user.token.test.config.annotation.ConsumedUserToken;
import com.bernardomg.security.user.token.test.config.annotation.ExpiredUserToken;
import com.bernardomg.security.user.token.test.config.annotation.RevokedUserToken;
import com.bernardomg.security.user.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.user.token.test.config.factory.UserTokens;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("UserTokenRepository - find all")
class ITUserTokenRepositoryFindAll {

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Test
    @DisplayName("Returns a token when the token is consumed")
    @OnlyUser
    @ConsumedUserToken
    void testFindAll_Consumed() {
        final Pagination      pagination;
        final Sorting         sorting;
        final Page<UserToken> tokens;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        tokens = userTokenRepository.findAll(pagination, sorting);

        // THEN
        Assertions.assertThat(tokens)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("tokens")
            .containsExactly(UserTokens.consumed());
    }

    @Test
    @DisplayName("Returns a token when the token is expired")
    @OnlyUser
    @ExpiredUserToken
    void testFindAll_Expired() {
        final Pagination      pagination;
        final Sorting         sorting;
        final Page<UserToken> tokens;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        tokens = userTokenRepository.findAll(pagination, sorting);

        // THEN
        Assertions.assertThat(tokens)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("tokens")
            .containsExactly(UserTokens.expired());
    }

    @Test
    @DisplayName("Doesn't return anything when there is no data")
    @OnlyUser
    void testFindAll_NoData() {
        final Pagination      pagination;
        final Sorting         sorting;
        final Page<UserToken> tokens;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        tokens = userTokenRepository.findAll(pagination, sorting);

        // THEN
        Assertions.assertThat(tokens)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("tokens")
            .isEmpty();
    }

    @Test
    @DisplayName("Returns a token when the token is revoked")
    @OnlyUser
    @RevokedUserToken
    void testFindAll_Revoked() {
        final Pagination      pagination;
        final Sorting         sorting;
        final Page<UserToken> tokens;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        tokens = userTokenRepository.findAll(pagination, sorting);

        // THEN
        Assertions.assertThat(tokens)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("tokens")
            .containsExactly(UserTokens.revoked());
    }

    @Test
    @DisplayName("Returns a token when the token is valid")
    @OnlyUser
    @ValidUserToken
    void testFindAll_Valid() {
        final Pagination      pagination;
        final Sorting         sorting;
        final Page<UserToken> tokens;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        // WHEN
        tokens = userTokenRepository.findAll(pagination, sorting);

        // THEN
        Assertions.assertThat(tokens)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("tokens")
            .containsExactly(UserTokens.valid());
    }

}
