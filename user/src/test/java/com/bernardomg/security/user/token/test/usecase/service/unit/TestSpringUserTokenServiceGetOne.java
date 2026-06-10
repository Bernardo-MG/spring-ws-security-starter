
package com.bernardomg.security.user.token.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.user.domain.exception.MissingUserTokenException;
import com.bernardomg.security.user.domain.model.UserToken;
import com.bernardomg.security.user.domain.repository.UserTokenRepository;
import com.bernardomg.security.user.token.test.config.factory.UserTokens;
import com.bernardomg.security.user.usecase.service.SpringUserTokenService;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringUserTokenService - get one")
class TestSpringUserTokenServiceGetOne {

    @InjectMocks
    private SpringUserTokenService service;

    @Mock
    private UserTokenRepository    userTokenRepository;

    @Test
    @DisplayName("When there is a token it is returned")
    void testGetOne() {
        final Optional<UserToken> token;
        final Optional<UserToken> existing;

        // GIVEN
        existing = Optional.of(UserTokens.valid());
        given(userTokenRepository.findOne(Tokens.TOKEN)).willReturn(existing);

        // WHEN
        token = service.getOne(Tokens.TOKEN);

        // THEN
        Assertions.assertThat(token)
            .as("token")
            .contains(UserTokens.valid());
    }

    @Test
    @DisplayName("When the token doesn't exist an exception is thrown")
    void testGetOne_NotExisting() {
        final ThrowingCallable executable;

        // GIVEN
        given(userTokenRepository.findOne(Tokens.TOKEN)).willReturn(Optional.empty());

        executable = () -> service.getOne(Tokens.TOKEN);

        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingUserTokenException.class);
    }

}
