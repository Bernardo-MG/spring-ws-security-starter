
package com.bernardomg.security.authorization.token.test.store.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.security.authorization.token.exception.MissingUserTokenCodeException;
import com.bernardomg.security.authorization.token.persistence.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.store.PersistentUserTokenStore;
import com.bernardomg.security.authorization.token.test.config.annotation.UserRegisteredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.model.UserTokens;
import com.bernardomg.security.config.authorization.UserTokenProperties;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("PersistentUserTokenStore - get username")
class ITPersistentUserTokenStoreGetUsername {

    private PersistentUserTokenStore store;

    @Autowired
    private UserTokenProperties      tokenProperties;

    @Autowired
    private UserRepository           userRepository;

    @Autowired
    private UserTokenRepository      userTokenRepository;

    @BeforeEach
    public void initialize() {
        store = new PersistentUserTokenStore(userTokenRepository, userRepository, UserTokens.SCOPE,
            tokenProperties.getValidity());
    }

    @Test
    @DisplayName("Extracts the username from a token")
    @OnlyUser
    @ValidUserToken
    void testGetUsername() {
        final String subject;

        subject = store.getUsername(UserTokens.TOKEN);

        Assertions.assertThat(subject)
            .isEqualTo(Users.USERNAME);
    }

    @Test
    @DisplayName("Extracts no username from a not existing token")
    @OnlyUser
    void testGetUsername_notExisting() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> store.getUsername(UserTokens.TOKEN);

        exception = Assertions.catchThrowableOfType(executable, MissingUserTokenCodeException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Missing token " + UserTokens.TOKEN);
    }

    @Test
    @DisplayName("Extracts no username from an out of scope token")
    @OnlyUser
    @UserRegisteredUserToken
    void testGetUsername_outOfScope() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> store.getUsername(UserTokens.TOKEN);

        exception = Assertions.catchThrowableOfType(executable, MissingUserTokenCodeException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Missing token " + UserTokens.TOKEN);
    }

}
