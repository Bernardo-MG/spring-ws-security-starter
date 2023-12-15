
package com.bernardomg.security.login.test.service.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authentication.jwt.token.test.config.TokenConstants;
import com.bernardomg.security.authentication.user.test.config.DisabledUser;
import com.bernardomg.security.authentication.user.test.config.ExpiredPasswordUser;
import com.bernardomg.security.authentication.user.test.config.ExpiredUser;
import com.bernardomg.security.authentication.user.test.config.LockedUser;
import com.bernardomg.security.authentication.user.test.config.ValidUser;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.security.authorization.role.test.config.UserWithNotGrantedPermissions;
import com.bernardomg.security.authorization.role.test.config.UserWithoutPermissions;
import com.bernardomg.security.login.model.TokenLoginStatus;
import com.bernardomg.security.login.service.TokenLoginService;
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

        status = service.login(Users.USERNAME, Users.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Logs in with a valid email")
    @ValidUser
    void testLogIn_Email_Valid() {
        final TokenLoginStatus status;

        status = service.login(Users.USERNAME, Users.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isTrue();
        Assertions.assertThat(status.getToken())
            .isNotBlank();
    }

    @Test
    @DisplayName("Doesn't log in an expired user")
    @ExpiredUser
    void testLogIn_Expired() {
        final TokenLoginStatus status;

        status = service.login(Users.USERNAME, Users.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in a locked user")
    @LockedUser
    void testLogIn_Locked() {
        final TokenLoginStatus status;

        status = service.login(Users.USERNAME, Users.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in a user with no permissions")
    @UserWithoutPermissions
    void testLogIn_NoPermissions() {
        final TokenLoginStatus status;

        status = service.login(Users.USERNAME, Users.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in a not existing user")
    @ValidUser
    void testLogIn_NotExisting() {
        final TokenLoginStatus status;

        status = service.login("abc", Users.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in a user with no granted permissions")
    @UserWithNotGrantedPermissions
    void testLogIn_NotGrantedPermissions() {
        final TokenLoginStatus status;

        status = service.login(Users.USERNAME, Users.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in a user with a expired password")
    @ExpiredPasswordUser
    void testLogIn_PasswordExpired() {
        final TokenLoginStatus status;

        status = service.login(Users.USERNAME, Users.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Logs in with a valid user")
    @ValidUser
    void testLogIn_Valid() {
        final TokenLoginStatus status;

        status = service.login(Users.USERNAME, Users.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isTrue();
        Assertions.assertThat(status.getToken())
            .isNotBlank();
    }

    @Test
    @DisplayName("Logs in with a valid user, ignoring username case")
    @ValidUser
    void testLogIn_Valid_Case() {
        final TokenLoginStatus status;

        status = service.login(Users.USERNAME.toUpperCase(), Users.PASSWORD);

        Assertions.assertThat(status.isLogged())
            .isTrue();
        Assertions.assertThat(status.getToken())
            .isNotBlank();
    }

    @Test
    @DisplayName("On a succesful login returns a valid JWT token")
    @ValidUser
    void testLogIn_Valid_JwtToken() {
        final TokenLoginStatus status;
        final JwtParser        parser;
        final Claims           claims;

        status = service.login(Users.USERNAME, Users.PASSWORD);

        parser = Jwts.parser()
            .verifyWith(TokenConstants.KEY)
            .build();

        claims = parser.parseSignedClaims(status.getToken())
            .getPayload();

        Assertions.assertThat(claims.getSubject())
            .isEqualTo(Users.USERNAME);
    }

}
