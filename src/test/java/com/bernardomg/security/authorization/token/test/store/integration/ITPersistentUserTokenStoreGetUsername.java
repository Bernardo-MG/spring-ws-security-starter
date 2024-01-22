
package com.bernardomg.security.authorization.token.test.store.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.token.adapter.inbound.jpa.repository.UserTokenSpringRepository;
import com.bernardomg.security.authorization.token.domain.exception.MissingUserTokenCodeException;
import com.bernardomg.security.authorization.token.store.PersistentUserTokenStore;
import com.bernardomg.security.authorization.token.test.config.annotation.UserRegisteredUserToken;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokenConstants;
import com.bernardomg.security.config.authorization.UserTokenProperties;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("PersistentUserTokenStore - get username")
class ITPersistentUserTokenStoreGetUsername {

    private PersistentUserTokenStore  store;

    @Autowired
    private UserTokenProperties       tokenProperties;

    @Autowired
    private UserSpringRepository      userRepository;

    @Autowired
    private UserTokenSpringRepository userTokenRepository;

    @BeforeEach
    public void initialize() {
        store = new PersistentUserTokenStore(userTokenRepository, userRepository, UserTokenConstants.SCOPE,
            tokenProperties.getValidity());
    }

    @Test
    @DisplayName("Extracts the username from a token")
    @OnlyUser
    @ValidUserToken
    void testGetUsername() {
        final String subject;

        subject = store.getUsername(UserTokenConstants.TOKEN);

        Assertions.assertThat(subject)
            .isEqualTo(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("Extracts no username from a not existing token")
    @OnlyUser
    void testGetUsername_notExisting() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> store.getUsername(UserTokenConstants.TOKEN);

        exception = Assertions.catchThrowableOfType(executable, MissingUserTokenCodeException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Missing token " + UserTokenConstants.TOKEN);
    }

    @Test
    @DisplayName("Extracts no username from an out of scope token")
    @OnlyUser
    @UserRegisteredUserToken
    void testGetUsername_outOfScope() {
        final ThrowingCallable executable;
        final Exception        exception;

        executable = () -> store.getUsername(UserTokenConstants.TOKEN);

        exception = Assertions.catchThrowableOfType(executable, MissingUserTokenCodeException.class);

        Assertions.assertThat(exception.getMessage())
            .isEqualTo("Missing token " + UserTokenConstants.TOKEN);
    }

}
