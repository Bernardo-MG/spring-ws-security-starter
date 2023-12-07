
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
import com.bernardomg.security.authorization.role.config.UserWithNotGrantedPermissions;
import com.bernardomg.security.authorization.role.config.UserWithoutPermissions;
import com.bernardomg.security.login.model.LoginStatus;
import com.bernardomg.security.login.model.TokenLoginStatus;
import com.bernardomg.security.login.model.request.LoginRequest;
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
        final LoginStatus  status;
        final LoginRequest login;

        login = new LoginRequest();
        login.setUsername(Users.USERNAME);
        login.setPassword(Users.PASSWORD);

        status = service.login(login);

        Assertions.assertThat(status)
            .isNotExactlyInstanceOf(TokenLoginStatus.class);

        Assertions.assertThat(status.getLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Logs in with a valid email")
    @ValidUser
    void testLogIn_Email_Valid() {
        final LoginStatus  status;
        final LoginRequest login;

        login = new LoginRequest();
        login.setUsername(Users.EMAIL);
        login.setPassword(Users.PASSWORD);

        status = service.login(login);

        Assertions.assertThat(status)
            .isInstanceOf(TokenLoginStatus.class);

        Assertions.assertThat(status.getLogged())
            .isTrue();
        Assertions.assertThat(((TokenLoginStatus) status).getToken())
            .isNotBlank();
    }

    @Test
    @DisplayName("Doesn't log in an expired user")
    @ExpiredUser
    void testLogIn_Expired() {
        final LoginStatus  status;
        final LoginRequest login;

        login = new LoginRequest();
        login.setUsername(Users.USERNAME);
        login.setPassword(Users.PASSWORD);

        status = service.login(login);

        Assertions.assertThat(status)
            .isNotExactlyInstanceOf(TokenLoginStatus.class);

        Assertions.assertThat(status.getLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in a locked user")
    @LockedUser
    void testLogIn_Locked() {
        final LoginStatus  status;
        final LoginRequest login;

        login = new LoginRequest();
        login.setUsername(Users.USERNAME);
        login.setPassword(Users.PASSWORD);

        status = service.login(login);

        Assertions.assertThat(status)
            .isNotExactlyInstanceOf(TokenLoginStatus.class);

        Assertions.assertThat(status.getLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in a user with no permissions")
    @UserWithoutPermissions
    void testLogIn_NoPermissions() {
        final LoginStatus  status;
        final LoginRequest login;

        login = new LoginRequest();
        login.setUsername(Users.USERNAME);
        login.setPassword(Users.PASSWORD);

        status = service.login(login);

        Assertions.assertThat(status)
            .isNotExactlyInstanceOf(TokenLoginStatus.class);

        Assertions.assertThat(status.getLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in a not existing user")
    @ValidUser
    void testLogIn_NotExisting() {
        final LoginStatus  status;
        final LoginRequest login;

        login = new LoginRequest();
        login.setUsername("abc");
        login.setPassword(Users.PASSWORD);

        status = service.login(login);

        Assertions.assertThat(status)
            .isNotExactlyInstanceOf(TokenLoginStatus.class);

        Assertions.assertThat(status.getLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in a user with no granted permissions")
    @UserWithNotGrantedPermissions
    void testLogIn_NotGrantedPermissions() {
        final LoginStatus  status;
        final LoginRequest login;

        login = new LoginRequest();
        login.setUsername(Users.USERNAME);
        login.setPassword(Users.PASSWORD);

        status = service.login(login);

        Assertions.assertThat(status)
            .isNotExactlyInstanceOf(TokenLoginStatus.class);

        Assertions.assertThat(status.getLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Doesn't log in a user with a expired password")
    @ExpiredPasswordUser
    void testLogIn_PasswordExpired() {
        final LoginStatus  status;
        final LoginRequest login;

        login = new LoginRequest();
        login.setUsername(Users.USERNAME);
        login.setPassword(Users.PASSWORD);

        status = service.login(login);

        Assertions.assertThat(status)
            .isNotInstanceOf(TokenLoginStatus.class);

        Assertions.assertThat(status.getLogged())
            .isFalse();
    }

    @Test
    @DisplayName("Logs in with a valid user")
    @ValidUser
    void testLogIn_Valid() {
        final LoginStatus  status;
        final LoginRequest login;

        login = new LoginRequest();
        login.setUsername(Users.USERNAME);
        login.setPassword(Users.PASSWORD);

        status = service.login(login);

        Assertions.assertThat(status)
            .isInstanceOf(TokenLoginStatus.class);

        Assertions.assertThat(status.getLogged())
            .isTrue();
        Assertions.assertThat(((TokenLoginStatus) status).getToken())
            .isNotBlank();
    }

    @Test
    @DisplayName("Logs in with a valid user, ignoring username case")
    @ValidUser
    void testLogIn_Valid_Case() {
        final LoginStatus  status;
        final LoginRequest login;

        login = new LoginRequest();
        login.setUsername(Users.USERNAME.toUpperCase());
        login.setPassword(Users.PASSWORD);

        status = service.login(login);

        Assertions.assertThat(status)
            .isInstanceOf(TokenLoginStatus.class);

        Assertions.assertThat(status.getLogged())
            .isTrue();
        Assertions.assertThat(((TokenLoginStatus) status).getToken())
            .isNotBlank();
    }

    @Test
    @DisplayName("On a succesful login returns a valid JWT token")
    @ValidUser
    void testLogIn_Valid_JwtToken() {
        final LoginStatus  status;
        final LoginRequest login;
        final JwtParser    parser;
        final Claims       claims;

        login = new LoginRequest();
        login.setUsername(Users.USERNAME);
        login.setPassword(Users.PASSWORD);

        status = service.login(login);

        parser = Jwts.parserBuilder()
            .setSigningKey(TokenConstants.KEY)
            .build();

        claims = parser.parseClaimsJws(((TokenLoginStatus) status).getToken())
            .getBody();

        Assertions.assertThat(claims.getSubject())
            .isEqualTo(Users.USERNAME);
    }

}
