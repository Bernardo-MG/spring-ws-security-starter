
package com.bernardomg.security.springframework.test.audit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.bernardomg.security.springframework.audit.SecurityUserDetailsAuditorAware;
import com.bernardomg.security.springframework.test.user.config.factory.UserConstants;
import com.bernardomg.security.springframework.userdetails.SecurityUserDetails;

@ExtendWith(MockitoExtension.class)
@DisplayName("SecurityUserDetailsAuditorAware")
public class TestSecurityUserDetailsAuditorAware {

    @InjectMocks
    private SecurityUserDetailsAuditorAware auditorAware;

    @Mock
    private AuthenticationTrustResolver     trustResolver;

    @Mock
    private SecurityUserDetails             userDetails;

    @Test
    @DisplayName("When the current user has no id, then the auditor is empty")
    void testGetCurrentAuditor_CurrentUserIdIsNull() {
        final Authentication authentication;

        // GIVEN
        authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        given(userDetails.getId()).willReturn(null);
        given(trustResolver.isAuthenticated(authentication)).willReturn(true);

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        // WHEN
        final Optional<Long> result = auditorAware.getCurrentAuditor();

        // THEN
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("When the current user is authenticated, then the auditor is returned")
    void testGetCurrentAuditor_IsAuthenticated() {
        final Authentication authentication;

        // GIVEN
        authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        given(userDetails.getId()).willReturn(UserConstants.ID);
        given(trustResolver.isAuthenticated(authentication)).willReturn(true);

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        // WHEN
        final Optional<Long> result = auditorAware.getCurrentAuditor();

        // THEN
        assertThat(result).contains(UserConstants.ID);
    }

    @Test
    @DisplayName("When the current user is not authenticated, then the auditor is empty")
    void testGetCurrentAuditor_IsNotAuthenticated() {
        final Authentication authentication;

        // GIVEN
        authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        given(trustResolver.isAuthenticated(authentication)).willReturn(false);

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        // WHEN
        final Optional<Long> result = auditorAware.getCurrentAuditor();

        // THEN
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("When the current user auth is not of the expected type, then the auditor is returned")
    void testGetCurrentAuditor_IsNotCorrectAuth() {
        final Authentication authentication;

        // GIVEN
        authentication = mock(Authentication.class);

        given(trustResolver.isAuthenticated(authentication)).willReturn(true);

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        // WHEN
        final Optional<Long> result = auditorAware.getCurrentAuditor();

        // THEN
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("When the current user auth details is not of the expected type, then the auditor is returned")
    void testGetCurrentAuditor_IsNotCorrectDetails() {
        final Authentication authentication;
        final UserDetails    userDetails;

        // GIVEN
        userDetails = mock(UserDetails.class);
        authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        given(trustResolver.isAuthenticated(authentication)).willReturn(true);

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        // WHEN
        final Optional<Long> result = auditorAware.getCurrentAuditor();

        // THEN
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("When there is no authentication, then the auditor is empty")
    void testGetCurrentAuditor_NoAuthentication() {
        // WHEN
        final Optional<Long> result = auditorAware.getCurrentAuditor();

        // THEN
        assertThat(result).isEmpty();
    }

}
