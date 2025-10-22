
package com.bernardomg.security.test.access.springframework.interceptor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bernardomg.security.access.interceptor.ResourceAccessValidator;
import com.bernardomg.security.access.springframework.interceptor.SpringResourceAccessValidator;
import com.bernardomg.security.permission.test.config.factory.PermissionConstants;
import com.bernardomg.test.config.factory.Authentications;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringResourceAccessValidator")
class TestSpringResourceAccessValidator {

    private final ResourceAccessValidator validator = new SpringResourceAccessValidator();

    public TestSpringResourceAccessValidator() {
        super();
    }

    @Test
    @DisplayName("An authorized user is authorized")
    void testIsAuthorized() {
        final Boolean authorized;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(Authentications.authenticatedWithCreateAuthorities());

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.DATA, PermissionConstants.CREATE);

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
        authorized = validator.isAuthorized(PermissionConstants.DATA, PermissionConstants.CREATE);

        // THEN
        Assertions.assertThat(authorized)
            .isFalse();
    }

    @Test
    @DisplayName("When the user has no authorities, it is not authorized")
    void testIsAuthorized_NoAuthorities() {
        final Boolean authorized;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(Authentications.authenticated());

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.DATA, PermissionConstants.CREATE);

        // THEN
        Assertions.assertThat(authorized)
            .isFalse();
    }

    @Test
    @DisplayName("When the user is not authenticated, it is not authorized")
    void testIsAuthorized_NotAuthenticated() {
        final Boolean authorized;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(Authentications.notAuthenticated());

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.DATA, PermissionConstants.CREATE);

        // THEN
        Assertions.assertThat(authorized)
            .isFalse();
    }

    @Test
    @DisplayName("When no authority matches the permission action, the user is not authorized")
    void testIsAuthorized_NotMatchingActionAuthorities() {
        final Boolean authorized;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(Authentications.authenticatedWithReadAuthorities());

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.DATA, PermissionConstants.CREATE);

        // THEN
        Assertions.assertThat(authorized)
            .isFalse();
    }

    @Test
    @DisplayName("When no authority matches the permission resource, the user is not authorized")
    void testIsAuthorized_NotMatchingResourceAuthorities() {
        final Boolean authorized;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(Authentications.authenticatedWithAlternativeCreateAuthorities());

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.DATA, PermissionConstants.CREATE);

        // THEN
        Assertions.assertThat(authorized)
            .isFalse();
    }

    @Test
    @DisplayName("When using simple authorities the user is not authorized")
    void testIsAuthorized_SimpleAuthorities() {
        final Boolean authorized;

        // GIVEN
        SecurityContextHolder.getContext()
            .setAuthentication(Authentications.authenticatedWithSimpleAuthorities());

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.DATA, PermissionConstants.CREATE);

        // THEN
        Assertions.assertThat(authorized)
            .isFalse();
    }

}
