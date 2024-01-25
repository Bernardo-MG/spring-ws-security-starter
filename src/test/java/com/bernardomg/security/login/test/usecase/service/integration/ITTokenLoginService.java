
package com.bernardomg.security.login.test.usecase.service.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.jwt.token.test.config.Tokens;
import com.bernardomg.security.authentication.user.test.config.annotation.DisabledUser;
import com.bernardomg.security.authentication.user.test.config.annotation.ExpiredPasswordUser;
import com.bernardomg.security.authentication.user.test.config.annotation.ExpiredUser;
import com.bernardomg.security.authentication.user.test.config.annotation.LockedUser;
import com.bernardomg.security.authentication.user.test.config.annotation.ValidUser;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.permission.test.config.annotation.UserWithCrudPermissionsNotGranted;
import com.bernardomg.security.authorization.permission.test.config.annotation.UserWithoutPermissions;
import com.bernardomg.security.login.domain.model.TokenLoginStatus;
import com.bernardomg.security.login.test.config.factory.TokenLoginStatuses;
import com.bernardomg.security.login.usecase.service.TokenLoginService;
import com.bernardomg.test.config.annotation.IntegrationTest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

@IntegrationTest
@DisplayName("TokenLoginService")
class ITTokenLoginService {

    @Autowired
    private TokenLoginService service;

    public ITTokenLoginService() {
        super();
    }

    @Test
    @DisplayName("Doesn't log in a disabled user")
    @DisabledUser
    void testLogIn_Disabled() {
        final TokenLoginStatus status;

        // WHEN
        status = service.login(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(status)
            .isEqualTo(TokenLoginStatuses.notLogged());
    }

    @Test
    @DisplayName("Doesn't log in an expired user")
    @ExpiredUser
    void testLogIn_Expired() {
        final TokenLoginStatus status;

        // WHEN
        status = service.login(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(status)
            .isEqualTo(TokenLoginStatuses.notLogged());
    }

    @Test
    @DisplayName("Doesn't log in with an invalid password")
    @ValidUser
    void testLogIn_InvalidPassword() {
        final TokenLoginStatus status;

        // WHEN
        status = service.login(UserConstants.USERNAME, "abc");

        // THEN
        Assertions.assertThat(status)
            .isEqualTo(TokenLoginStatuses.notLogged());
    }

    @Test
    @DisplayName("Doesn't log in a locked user")
    @LockedUser
    void testLogIn_Locked() {
        final TokenLoginStatus status;

        // WHEN
        status = service.login(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(status)
            .isEqualTo(TokenLoginStatuses.notLogged());
    }

    @Test
    @DisplayName("Doesn't log in a user with no permissions")
    @UserWithoutPermissions
    void testLogIn_NoPermissions() {
        final TokenLoginStatus status;

        // WHEN
        status = service.login(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(status)
            .isEqualTo(TokenLoginStatuses.notLogged());
    }

    @Test
    @DisplayName("Doesn't log in a not existing user")
    void testLogIn_NotExisting() {
        final TokenLoginStatus status;

        // WHEN
        status = service.login(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(status)
            .isEqualTo(TokenLoginStatuses.notLogged());
    }

    @Test
    @DisplayName("Doesn't log in a user with no granted permissions")
    @UserWithCrudPermissionsNotGranted
    void testLogIn_NotGrantedPermissions() {
        final TokenLoginStatus status;

        // WHEN
        status = service.login(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(status)
            .isEqualTo(TokenLoginStatuses.notLogged());
    }

    @Test
    @DisplayName("Doesn't log in a user with a expired password")
    @ExpiredPasswordUser
    void testLogIn_PasswordExpired() {
        final TokenLoginStatus status;

        // WHEN
        status = service.login(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        Assertions.assertThat(status)
            .isEqualTo(TokenLoginStatuses.notLogged());
    }

    @Test
    @DisplayName("Logs in with a valid user")
    @ValidUser
    void testLogIn_Valid() {
        final TokenLoginStatus status;

        // WHEN
        status = service.login(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(status.isLogged())
                .isTrue();
            softly.assertThat(status.getToken())
                .isNotBlank();
        });
    }

    @Test
    @DisplayName("Logs in with a valid email")
    @ValidUser
    void testLogIn_Valid_EmailLogin() {
        final TokenLoginStatus status;

        // WHEN
        status = service.login(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(status.isLogged())
                .isTrue();
            softly.assertThat(status.getToken())
                .isNotBlank();
        });
    }

    @Test
    @DisplayName("On a succesful login returns a valid JWT token")
    @ValidUser
    void testLogIn_Valid_JwtToken() {
        final TokenLoginStatus status;
        final JwtParser        parser;
        final Claims           claims;

        // WHEN
        status = service.login(UserConstants.USERNAME, UserConstants.PASSWORD);

        // THEN
        parser = Jwts.parser()
            .verifyWith(Tokens.KEY)
            .build();

        claims = parser.parseSignedClaims(status.getToken())
            .getPayload();

        Assertions.assertThat(claims.getSubject())
            .isEqualTo(UserConstants.USERNAME);
    }

    @Test
    @DisplayName("Logs in with a valid user, ignoring username case")
    @ValidUser
    void testLogIn_Valid_UsernameCase() {
        final TokenLoginStatus status;

        // WHEN
        status = service.login(UserConstants.USERNAME.toUpperCase(), UserConstants.PASSWORD);

        // THEN
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(status.isLogged())
                .isTrue();
            softly.assertThat(status.getToken())
                .isNotBlank();
        });
    }

}