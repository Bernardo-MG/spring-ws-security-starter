
package com.bernardomg.security.user.test.usecase.integration;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.user.data.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.user.data.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.user.data.adapter.inbound.jpa.repository.UserTokenSpringRepository;
import com.bernardomg.security.user.data.domain.model.UserTokenStatus;
import com.bernardomg.security.user.data.usecase.service.UserActivationService;
import com.bernardomg.security.user.data.usecase.service.UserService;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("Full new user register process")
class ITFullNewUserRegisterProcess {

    @Autowired
    private PasswordEncoder           passwordEncoder;

    @Autowired
    private UserActivationService     userActivationService;

    @Autowired
    private UserSpringRepository      userRepository;

    @Autowired
    private UserService               userService;

    @Autowired
    private UserTokenSpringRepository userTokenRepository;

    public ITFullNewUserRegisterProcess() {
        super();
    }

    private final void changeToAdmin() {
        final Authentication                         auth;
        final Collection<? extends GrantedAuthority> authorities;
        final GrantedAuthority                       authority;

        authority = new SimpleGrantedAuthority("USER:CREATE");
        authorities = List.of(authority);

        auth = new UsernamePasswordAuthenticationToken(UserConstants.USERNAME, UserConstants.PASSWORD, authorities);
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
        userService.registerNewUser(UserConstants.USERNAME, UserConstants.NAME, UserConstants.EMAIL);

        // Validate new token
        token = userTokenRepository.findAll()
            .stream()
            .findFirst()
            .get()
            .getToken();

        validTokenStatus = userActivationService.validateToken(token);

        Assertions.assertThat(validTokenStatus.valid())
            .isTrue();
        Assertions.assertThat(validTokenStatus.username())
            .isEqualTo("username");

        // TODO: Set authentication to anonymous user
        changeToAnonymous();

        // Enable new user
        userActivationService.activateUser(token, UserConstants.NEW_PASSWORD);

        user = userRepository.findAll()
            .stream()
            .findFirst()
            .get();

        Assertions.assertThat(user.getEmail())
            .isEqualTo(UserConstants.EMAIL);
        Assertions.assertThat(user.getUsername())
            .isEqualTo(UserConstants.USERNAME);
        Assertions.assertThat(user.getName())
            .isEqualTo(UserConstants.NAME);
        Assertions.assertThat(user.getEnabled())
            .isTrue();
        Assertions.assertThat(passwordEncoder.matches(UserConstants.NEW_PASSWORD, user.getPassword()))
            .isTrue();
    }

}
