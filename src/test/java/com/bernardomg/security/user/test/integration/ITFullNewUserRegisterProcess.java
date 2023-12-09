
package com.bernardomg.security.user.test.integration;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bernardomg.security.authentication.user.persistence.model.UserEntity;
import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authentication.user.service.UserActivationService;
import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.security.authorization.token.model.UserTokenStatus;
import com.bernardomg.security.authorization.token.persistence.repository.UserTokenRepository;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("Full new user register process")
class ITFullNewUserRegisterProcess {

    @Autowired
    private UserActivationService userActivationService;

    @Autowired
    private UserRepository        userRepository;

    @Autowired
    private UserTokenRepository   userTokenRepository;

    public ITFullNewUserRegisterProcess() {
        super();
    }

    private final void changeToAdmin() {
        final Authentication                         auth;
        final Collection<? extends GrantedAuthority> authorities;
        final GrantedAuthority                       authority;

        authority = new SimpleGrantedAuthority("USER:CREATE");
        authorities = List.of(authority);

        auth = new UsernamePasswordAuthenticationToken("admin", "1234", authorities);
        SecurityContextHolder.getContext()
            .setAuthentication(auth);
    }

    private final void changeToAnonymous() {
        final Authentication                         auth;
        final Collection<? extends GrantedAuthority> authorities;
        final GrantedAuthority                       authority;

        authority = new SimpleGrantedAuthority("NOTHING");
        authorities = List.of(authority);

        auth = new AnonymousAuthenticationToken("anonymous", "principal", authorities);
        SecurityContextHolder.getContext()
            .setAuthentication(auth);
    }

    @Test
    @DisplayName("Can follow the new user process from start to end")
    void testNewUser_Valid() {
        final UserTokenStatus validTokenStatus;
        final String          token;
        final UserEntity      user;

        // TODO: Set authentication to admin
        changeToAdmin();

        // Register new user
        userActivationService.registerNewUser(Users.USERNAME, Users.NAME, Users.EMAIL);

        // Validate new token
        token = userTokenRepository.findAll()
            .stream()
            .findFirst()
            .get()
            .getToken();

        validTokenStatus = userActivationService.validateToken(token);

        Assertions.assertThat(validTokenStatus.isValid())
            .isTrue();
        Assertions.assertThat(validTokenStatus.getUsername())
            .isEqualTo("username");

        // TODO: Set authentication to anonymous user
        changeToAnonymous();

        // Enable new user
        userActivationService.activateUser(token, "1234");

        user = userRepository.findAll()
            .stream()
            .findFirst()
            .get();

        Assertions.assertThat(user.getEmail())
            .isEqualTo(Users.EMAIL);
        Assertions.assertThat(user.getUsername())
            .isEqualTo(Users.USERNAME);
        Assertions.assertThat(user.getName())
            .isEqualTo(Users.NAME);
        Assertions.assertThat(user.getEnabled())
            .isTrue();
        Assertions.assertThat(user.getPassword())
            .isEqualTo(Users.ENCODED_PASSWORD);
    }

}
