
package com.bernardomg.security.authorization.token.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.authorization.token.domain.model.UserToken;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokens;
import com.bernardomg.security.authorization.token.usecase.service.SpringUserTokenService;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringUserTokenService - clean up")
class TestSpringUserTokenServiceCleanUp {

    @InjectMocks
    private SpringUserTokenService service;

    @Mock
    private UserTokenRepository    userTokenRepository;

    @Test
    @DisplayName("Sends the finished tokens to delete")
    void testCleanUpTokens_DeletesFinished() {
        final Collection<UserToken> existing;

        // GIVEN
        existing = List.of(UserTokens.valid());
        given(userTokenRepository.findAllFinished()).willReturn(existing);

        // WHEN
        service.cleanUpTokens();

        // THEN
        verify(userTokenRepository).deleteAll(existing);
    }

}
