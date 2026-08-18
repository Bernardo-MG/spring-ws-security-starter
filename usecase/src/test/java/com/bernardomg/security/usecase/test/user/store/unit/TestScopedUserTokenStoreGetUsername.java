
package com.bernardomg.security.usecase.test.user.store.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.domain.user.exception.MissingUserTokenException;
import com.bernardomg.security.domain.user.model.UserToken;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.domain.user.repository.UserTokenRepository;
import com.bernardomg.security.usecase.test.user.config.factory.UserTokenConstants;
import com.bernardomg.security.usecase.test.user.config.factory.UserTokens;
import com.bernardomg.security.usecase.user.store.ScopedUserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("Scoped user token store - get username")
class TestScopedUserTokenStoreGetUsername {

    private ScopedUserTokenStore store;

    @Mock
    private UserRepository       userRepository;

    @Mock
    private UserTokenRepository  userTokenRepository;

    public TestScopedUserTokenStoreGetUsername() {
        super();
    }

    @BeforeEach
    void setUp() {
        store = new ScopedUserTokenStore(userTokenRepository, userRepository, UserTokenConstants.SCOPE,
            UserTokenConstants.VALIDITY, UserTokenConstants.NAME);
    }

    @Test
    @DisplayName("When the token exists its username is returned")
    void testGetUsername() {
        final UserToken userToken;
        final String    result;

        // GIVEN
        userToken = UserTokens.valid();

        given(userTokenRepository.findOneByScope(UserTokenConstants.TOKEN, UserTokenConstants.SCOPE))
            .willReturn(Optional.of(userToken));

        // WHEN
        result = store.getUsername(UserTokenConstants.TOKEN);

        // THEN
        assertThat(result).as("username")
            .isEqualTo(UserTokenConstants.USERNAME);
    }

    @Test
    @DisplayName("When the token doesn't exist an exception is thrown")
    void testGetUsername_MissingToken() {
        final ThrowingCallable execution;

        // GIVEN
        given(userTokenRepository.findOneByScope(UserTokenConstants.TOKEN, UserTokenConstants.SCOPE))
            .willReturn(Optional.empty());

        // WHEN
        execution = () -> store.getUsername(UserTokenConstants.TOKEN);

        // THEN
        assertThatThrownBy(execution).isInstanceOf(MissingUserTokenException.class);
    }

}
