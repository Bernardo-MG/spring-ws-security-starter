
package com.bernardomg.security.user.token.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.user.data.domain.model.UserToken;
import com.bernardomg.security.user.data.domain.repository.UserTokenRepository;
import com.bernardomg.security.user.data.usecase.service.SpringUserTokenService;
import com.bernardomg.security.user.token.test.config.factory.UserTokens;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringUserTokenService - get all")
class TestSpringUserTokenServiceGetAll {

    @InjectMocks
    private SpringUserTokenService service;

    @Mock
    private UserTokenRepository    userTokenRepository;

    @Test
    @DisplayName("When there are tokens they are returned")
    void testGetAll() {
        final Pagination      pagination;
        final Sorting         sorting;
        final Page<UserToken> tokens;
        final Page<UserToken> existing;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        existing = new Page<>(List.of(UserTokens.valid()), 0, 0, 0, 0, 0, false, false, sorting);
        given(userTokenRepository.findAll(pagination, sorting)).willReturn(existing);

        // WHEN
        tokens = service.getAll(pagination, sorting);

        // THEN
        Assertions.assertThat(tokens)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("tokens")
            .containsExactly(UserTokens.valid());
    }

    @Test
    @DisplayName("When there are no tokens nothing is returned")
    void testGetAll_NoData() {
        final Pagination      pagination;
        final Sorting         sorting;
        final Page<UserToken> tokens;
        final Page<UserToken> existing;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        existing = new Page<>(List.of(), 0, 0, 0, 0, 0, false, false, sorting);
        given(userTokenRepository.findAll(pagination, sorting)).willReturn(existing);

        // WHEN
        tokens = service.getAll(pagination, sorting);

        // THEN
        Assertions.assertThat(tokens)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("tokens")
            .isEmpty();
    }

    @Test
    @DisplayName("The pagination data is sent to the repository")
    void testGetAll_Pagination() {
        final Pagination      pagination;
        final Sorting         sorting;
        final Page<UserToken> existing;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        existing = new Page<>(List.of(UserTokens.valid()), 0, 0, 0, 0, 0, false, false, sorting);
        given(userTokenRepository.findAll(pagination, sorting)).willReturn(existing);

        // WHEN
        service.getAll(pagination, sorting);

        // THEN
        verify(userTokenRepository).findAll(pagination, sorting);
    }

}
