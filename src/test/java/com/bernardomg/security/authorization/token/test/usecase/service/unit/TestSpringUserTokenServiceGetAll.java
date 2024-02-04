
package com.bernardomg.security.authorization.token.test.usecase.service.unit;

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
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.token.domain.model.UserToken;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokens;
import com.bernardomg.security.authorization.token.usecase.service.SpringUserTokenService;

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
        final Pageable            pageable;
        final Iterable<UserToken> tokens;
        final Iterable<UserToken> existing;

        // GIVEN
        pageable = Pageable.unpaged();

        existing = List.of(UserTokens.valid());
        given(userTokenRepository.findAll(pageable)).willReturn(existing);

        // WHEN
        tokens = service.getAll(pageable);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .containsExactly(UserTokens.valid());
    }

    @Test
    @DisplayName("When there are no tokens nothing is returned")
    void testGetAll_NoData() {
        final Pageable            pageable;
        final Iterable<UserToken> tokens;
        final Iterable<UserToken> existing;

        // GIVEN
        pageable = Pageable.unpaged();

        existing = List.of();
        given(userTokenRepository.findAll(pageable)).willReturn(existing);

        // WHEN
        tokens = service.getAll(pageable);

        // THEN
        Assertions.assertThat(tokens)
            .as("tokens")
            .isEmpty();
    }

    @Test
    @DisplayName("The pagination data is sent to the repository")
    void testGetAll_Pagination() {
        final Pageable            pageable;
        final Iterable<UserToken> existing;

        // GIVEN
        pageable = Pageable.unpaged();

        existing = List.of(UserTokens.valid());
        given(userTokenRepository.findAll(pageable)).willReturn(existing);

        // WHEN
        service.getAll(pageable);

        // THEN
        verify(userTokenRepository).findAll(pageable);
    }

}
