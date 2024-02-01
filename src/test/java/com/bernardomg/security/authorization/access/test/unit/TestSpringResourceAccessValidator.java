
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
import org.springframework.security.core.context.SecurityContextHolder;

import com.bernardomg.security.authorization.access.adapter.inbound.spring.SpringResourceAccessValidator;
import com.bernardomg.security.authorization.access.test.config.factory.GrantedAuthorities;
import com.bernardomg.security.authorization.access.usecase.validator.ResourceAccessValidator;
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
        return List.of(GrantedAuthorities.resource());
    }

    @SuppressWarnings("rawtypes")
    private final Collection getSimpleAuthorities() {
        return List.of(GrantedAuthorities.simple());
    }

    @Test
    @DisplayName("An authorized user is authorized")
    @SuppressWarnings("unchecked")
    void testIsAuthorized() {
        final Boolean authorized;

        // GIVEN
        given(authentication.isAuthenticated()).willReturn(true);
        given(authentication.getAuthorities()).willReturn(getAuthorities());

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.RESOURCE, PermissionConstants.ACTION);

        // THEN
        Assertions.assertThat(authorized)
            .isTrue();
    }

    @Test
    @DisplayName("When missing authentication the user is not authorized")
    void testIsAuthorized_MissingAuthentication() {
        final Boolean authorized;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(null);

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.RESOURCE, PermissionConstants.ACTION);

        // THEN
        Assertions.assertThat(authorized)
            .isFalse();
    }

    @Test
    @DisplayName("When the user has no authorities, it is not authorized")
    void testIsAuthorized_NoAuthorities() {
        final Boolean authorized;

        // GIVEN
        given(authentication.isAuthenticated()).willReturn(true);

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.RESOURCE, PermissionConstants.ACTION);

        // THEN
        Assertions.assertThat(authorized)
            .isFalse();
    }

    @Test
    @DisplayName("When the user is not authenticated, it is not authorized")
    void testIsAuthorized_NotAuthenticated() {
        final Boolean authorized;

        // GIVEN
        given(authentication.isAuthenticated()).willReturn(false);

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.RESOURCE, PermissionConstants.ACTION);

        // THEN
        Assertions.assertThat(authorized)
            .isFalse();
    }

    @Test
    @DisplayName("When using simple authorities the user is not authorized")
    @SuppressWarnings("unchecked")
    void testIsAuthorized_SimpleAuthorities() {
        final Boolean authorized;

        // GIVEN
        given(authentication.isAuthenticated()).willReturn(true);
        given(authentication.getAuthorities()).willReturn(getSimpleAuthorities());

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.RESOURCE, PermissionConstants.ACTION);

        // THEN
        Assertions.assertThat(authorized)
            .isFalse();
    }

}
