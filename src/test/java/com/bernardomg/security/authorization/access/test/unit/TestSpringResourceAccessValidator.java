
package com.bernardomg.security.authorization.access.test.unit;

import static org.mockito.BDDMockito.given;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bernardomg.security.authorization.access.adapter.inbound.spring.SpringResourceAccessValidator;
import com.bernardomg.security.authorization.access.test.config.factory.GrantedAuthorities;
import com.bernardomg.security.authorization.access.usecase.validator.ResourceAccessValidator;
import com.bernardomg.security.authorization.permission.adapter.inbound.spring.ResourceActionGrantedAuthority;
import com.bernardomg.security.authorization.permission.test.config.factory.PermissionConstants;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringResourceAccessValidator")
class TestSpringResourceAccessValidator {

    @Mock
    private Authentication                authentication;

    private final ResourceAccessValidator validator = new SpringResourceAccessValidator();

    public TestSpringResourceAccessValidator() {
        super();
    }

    @SuppressWarnings("rawtypes")
    private final Collection getAuthorities() {
        final ResourceActionGrantedAuthority authority;

        authority = GrantedAuthorities.resource();
        return List.of(authority);
    }

    @SuppressWarnings("rawtypes")
    private final Collection getSimpleAuthorities() {
        final SimpleGrantedAuthority authority;

        authority = GrantedAuthorities.simple();
        return List.of(authority);
    }

    @SuppressWarnings("unchecked")
    private final void initializeAuthenticated() {
        given(authentication.isAuthenticated()).willReturn(true);
        given(authentication.getAuthorities()).willReturn(getAuthorities());

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);
    }

    private final void initializeEmptyAuthentication() {
        SecurityContextHolder.getContext()
            .setAuthentication(null);
    }

    private final void initializeNoAuthorities() {
        given(authentication.isAuthenticated()).willReturn(true);

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);
    }

    private final void initializeNotAuthenticated() {
        given(authentication.isAuthenticated()).willReturn(false);

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);
    }

    @SuppressWarnings("unchecked")
    private final void initializeSimpleAuthenticated() {
        given(authentication.isAuthenticated()).willReturn(true);
        given(authentication.getAuthorities()).willReturn(getSimpleAuthorities());

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);
    }

    @Test
    @DisplayName("An authorized user is authorized")
    void testIsAuthorized() {
        final Boolean authorized;

        initializeAuthenticated();

        authorized = validator.isAuthorized(PermissionConstants.RESOURCE, PermissionConstants.ACTION);

        Assertions.assertThat(authorized)
            .isTrue();
    }

    @Test
    @DisplayName("When missing authentication the user is not authorized")
    void testIsAuthorized_MissingAuthentication() {
        final Boolean authorized;

        initializeEmptyAuthentication();

        authorized = validator.isAuthorized(PermissionConstants.RESOURCE, PermissionConstants.ACTION);

        Assertions.assertThat(authorized)
            .isFalse();
    }

    @Test
    @DisplayName("When the user has no authorities, it is not authorized")
    void testIsAuthorized_NoAuthorities() {
        final Boolean authorized;

        initializeNoAuthorities();

        authorized = validator.isAuthorized(PermissionConstants.RESOURCE, PermissionConstants.ACTION);

        Assertions.assertThat(authorized)
            .isFalse();
    }

    @Test
    @DisplayName("When the user is not authenticated, it is not authorized")
    void testIsAuthorized_NotAuthenticated() {
        final Boolean authorized;

        initializeNotAuthenticated();

        authorized = validator.isAuthorized(PermissionConstants.RESOURCE, PermissionConstants.ACTION);

        Assertions.assertThat(authorized)
            .isFalse();
    }

    @Test
    @DisplayName("When using simple authorities the user is not authorized")
    void testIsAuthorized_SimpleAuthorities() {
        final Boolean authorized;

        initializeSimpleAuthenticated();

        authorized = validator.isAuthorized(PermissionConstants.RESOURCE, PermissionConstants.ACTION);

        Assertions.assertThat(authorized)
            .isFalse();
    }

}
