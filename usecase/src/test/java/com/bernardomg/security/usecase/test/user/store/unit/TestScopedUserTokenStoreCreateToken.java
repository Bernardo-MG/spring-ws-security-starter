
package com.bernardomg.security.usecase.test.user.store.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.domain.user.exception.MissingUsernameException;
import com.bernardomg.security.domain.user.model.UserToken;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.domain.user.repository.UserTokenRepository;
import com.bernardomg.security.usecase.test.user.config.factory.UserTokenConstants;
import com.bernardomg.security.usecase.test.user.config.factory.UserTokens;
import com.bernardomg.security.usecase.user.store.ScopedUserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("Scoped user token store - create token")
class TestScopedUserTokenStoreCreateToken {

    private ScopedUserTokenStore store;

    @Mock
    private UserRepository       userRepository;

    @Mock
    private UserTokenRepository  userTokenRepository;

    public TestScopedUserTokenStoreCreateToken() {
        super();
    }

    @BeforeEach
    void setUp() {
        store = new ScopedUserTokenStore(userTokenRepository, userRepository, UserTokenConstants.SCOPE,
            UserTokenConstants.VALIDITY, UserTokenConstants.NAME);
    }

    @Test
    @DisplayName("When the user exists a token is created and returned")
    void testCreateToken() {
        final UserToken userToken;
        final String    result;

        // GIVEN
        userToken = UserTokens.valid();

        given(userRepository.exists(UserTokenConstants.USERNAME)).willReturn(true);
        given(userTokenRepository.save(any())).willReturn(userToken);

        // WHEN
        result = store.createToken(UserTokenConstants.USERNAME);

        // THEN
        assertThat(result).as("token")
            .isEqualTo(userToken.token());
    }

    @Test
    @DisplayName("When the user doesn't exist an exception is thrown")
    void testCreateToken_MissingUser() {
        final ThrowingCallable execution;

        // GIVEN
        given(userRepository.exists(UserTokenConstants.USERNAME)).willReturn(false);

        // WHEN
        execution = () -> store.createToken(UserTokenConstants.USERNAME);

        // THEN
        assertThatThrownBy(execution).isInstanceOf(MissingUsernameException.class);

        then(userTokenRepository).shouldHaveNoInteractions();
    }

}
