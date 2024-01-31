
package com.bernardomg.security.authorization.token.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authorization.token.domain.model.request.UserTokenPartial;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenPartials;
import com.bernardomg.security.authorization.token.usecase.service.SpringUserTokenService;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringUserTokenService - patch")
class TestSpringUserTokenServicePatch {

    @InjectMocks
    private SpringUserTokenService service;

    @Mock
    private UserTokenRepository    userTokenRepository;

    @Test
    @DisplayName("Patching sends the user token to the repository")
    void testPatch_Empty_Persisted() {
        final UserTokenPartial request;

        // GIVEN
        request = UserTokenPartials.empty();
        given(userTokenRepository.exists(Tokens.TOKEN)).willReturn(true);

        // WHEN
        service.patch(Tokens.TOKEN, request);

        // THEN
        verify(userTokenRepository, atLeastOnce()).patch(Tokens.TOKEN, request);
    }

}
