
package com.bernardomg.security.usecase.test.token.unit;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.domain.user.exception.MissingUsernameException;
import com.bernardomg.security.domain.user.model.User;
import com.bernardomg.security.domain.user.model.UserToken;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.domain.user.repository.UserTokenRepository;
import com.bernardomg.security.usecase.test.user.config.factory.UserTokenConstants;
import com.bernardomg.security.usecase.test.user.config.factory.UserTokens;
import com.bernardomg.security.usecase.token.ScopedUserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("Scoped user token store - revoke existing tokens")
class TestScopedUserTokenStoreRevokeExistingTokens {

    @Mock
    private UserToken            firstToken;

    @Mock
    private UserToken            secondToken;

    private ScopedUserTokenStore store;

    @Mock
    private User                 user;

    @Mock
    private UserRepository       userRepository;

    @Mock
    private UserTokenRepository  userTokenRepository;

    public TestScopedUserTokenStoreRevokeExistingTokens() {
        super();
    }

    @BeforeEach
    void setUp() {
        store = new ScopedUserTokenStore(userTokenRepository, userRepository, UserTokenConstants.SCOPE,
            UserTokenConstants.VALIDITY, UserTokenConstants.NAME);
    }

    @Test
    @DisplayName("When the user exists all active tokens are revoked")
    void testRevokeExistingTokens() {
        final UserToken        firstToRevoke;
        final UserToken        secondToRevoke;
        final UserToken        firstRevoked;
        final UserToken        secondRevoked;
        final List<UserToken>  tokensToRevoke;
        final List<UserToken>  tokensRevoked;
        final ThrowingCallable execution;

        // GIVEN
        firstToRevoke = UserTokens.valid();
        secondToRevoke = UserTokens.valid();
        tokensToRevoke = List.of(firstToRevoke, secondToRevoke);

        firstRevoked = UserTokens.revoked();
        secondRevoked = UserTokens.revoked();
        tokensRevoked = List.of(firstRevoked, secondRevoked);

        given(userRepository.findOne(UserTokenConstants.USERNAME)).willReturn(Optional.of(user));
        given(user.username()).willReturn(UserTokenConstants.USERNAME);
        given(userTokenRepository.findAllNotRevoked(UserTokenConstants.USERNAME, UserTokenConstants.SCOPE))
            .willReturn(tokensToRevoke);
        given(userTokenRepository.saveAll(tokensRevoked)).willReturn(tokensRevoked);

        // WHEN
        execution = () -> store.revokeExistingTokens(UserTokenConstants.USERNAME);

        // THEN
        assertThatCode(execution).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("When the user doesn't exist an exception is thrown")
    void testRevokeExistingTokens_MissingUser() {
        final ThrowingCallable execution;

        // GIVEN
        given(userRepository.findOne(UserTokenConstants.USERNAME)).willReturn(Optional.empty());

        // WHEN
        execution = () -> store.revokeExistingTokens(UserTokenConstants.USERNAME);

        // THEN
        assertThatThrownBy(execution).isInstanceOf(MissingUsernameException.class);
    }

    @Test
    @DisplayName("When there are no active tokens an empty collection is saved")
    void testRevokeExistingTokens_NoTokens() {
        final ThrowingCallable execution;

        // GIVEN
        given(userRepository.findOne(UserTokenConstants.USERNAME)).willReturn(Optional.of(user));
        given(user.username()).willReturn(UserTokenConstants.USERNAME);
        given(userTokenRepository.findAllNotRevoked(UserTokenConstants.USERNAME, UserTokenConstants.SCOPE))
            .willReturn(List.of());

        // WHEN
        execution = () -> store.revokeExistingTokens(UserTokenConstants.USERNAME);

        // THEN
        assertThatCode(execution).doesNotThrowAnyException();
    }

}
