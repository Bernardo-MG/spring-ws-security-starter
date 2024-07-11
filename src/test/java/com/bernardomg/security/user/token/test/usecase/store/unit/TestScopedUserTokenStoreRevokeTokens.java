
package com.bernardomg.security.user.token.test.usecase.store.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.jwt.test.config.Tokens;
import com.bernardomg.security.user.data.domain.exception.MissingUserException;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.test.config.factory.Users;
import com.bernardomg.security.user.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.user.token.test.config.factory.UserTokens;
import com.bernardomg.security.user.token.usecase.store.ScopedUserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("ScopedUserTokenStore - revoke existing tokens")
class TestScopedUserTokenStoreRevokeTokens {

    private ScopedUserTokenStore store;

    @Mock
    private UserRepository       userRepository;

    @Mock
    private UserTokenRepository  userTokenRepository;

    @BeforeEach
    public void initialize() {
        final Duration validity;

        validity = Duration.ofMinutes(1);
        store = new ScopedUserTokenStore(userTokenRepository, userRepository, Tokens.SCOPE, validity);
    }

    @Test
    @DisplayName("Revokes an existing token")
    void testRevokeExistingTokens() {
        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(userTokenRepository.findAllNotRevoked(UserConstants.USERNAME, Tokens.SCOPE))
            .willReturn(List.of(UserTokens.valid()));

        // WHEN
        store.revokeExistingTokens(UserConstants.USERNAME);

        // THEN
        verify(userTokenRepository).saveAll(List.of(UserTokens.revoked()));
    }

    @Test
    @DisplayName("When no token exists no token is modified")
    void testRevokeExistingTokens_NoData() {
        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.of(Users.enabled()));
        given(userTokenRepository.findAllNotRevoked(UserConstants.USERNAME, Tokens.SCOPE)).willReturn(List.of());

        // WHEN
        store.revokeExistingTokens(UserConstants.USERNAME);

        // THEN
        verify(userTokenRepository).saveAll(List.of());
    }

    @Test
    @DisplayName("When revoking a not existing user an exception is thrown")
    void testRevokeExistingTokens_NotExistingUser() {
        final ThrowingCallable executable;

        // GIVEN
        given(userRepository.findOne(UserConstants.USERNAME)).willReturn(Optional.empty());

        // WHEN
        executable = () -> store.revokeExistingTokens(UserConstants.USERNAME);

        // THEN
        Assertions.assertThatThrownBy(executable)
            .isInstanceOf(MissingUserException.class);
    }

}
