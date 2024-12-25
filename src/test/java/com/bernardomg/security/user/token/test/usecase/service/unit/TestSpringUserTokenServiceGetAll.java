
package com.bernardomg.security.user.token.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.user.token.domain.model.UserToken;
import com.bernardomg.security.user.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.user.token.test.config.factory.UserTokens;
import com.bernardomg.security.user.token.usecase.service.SpringUserTokenService;

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
        final Pagination          pagination;
        final Sorting             sorting;
        final Iterable<UserToken> tokens;
        final Iterable<UserToken> existing;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        existing = List.of(UserTokens.valid());
        given(userTokenRepository.findAll(pagination, sorting)).willReturn(existing);

        // WHEN
        tokens = service.getAll(pagination, sorting);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .containsExactly(UserTokens.valid());
    }

    @Test
    @DisplayName("When there are no tokens nothing is returned")
    void testGetAll_NoData() {
        final Pagination          pagination;
        final Sorting             sorting;
        final Iterable<UserToken> tokens;
        final Iterable<UserToken> existing;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        existing = List.of();
        given(userTokenRepository.findAll(pagination, sorting)).willReturn(existing);

        // WHEN
        tokens = service.getAll(pagination, sorting);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .isEmpty();
    }

    @Test
    @DisplayName("The pagination data is sent to the repository")
    void testGetAll_Pagination() {
        final Pagination          pagination;
        final Sorting             sorting;
        final Iterable<UserToken> existing;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        existing = List.of(UserTokens.valid());
        given(userTokenRepository.findAll(pagination, sorting)).willReturn(existing);

        // WHEN
        service.getAll(pagination, sorting);

        // THEN
        verify(userTokenRepository).findAll(pagination, sorting);
    }

}
