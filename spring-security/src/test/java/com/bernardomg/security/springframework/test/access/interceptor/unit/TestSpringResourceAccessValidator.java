
package com.bernardomg.security.springframework.test.access.interceptor.unit;

import static org.mockito.Mockito.verifyNoInteractions;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bernardomg.security.springframework.access.interceptor.ResourcePermissionEvaluator;
import com.bernardomg.security.springframework.access.interceptor.SpringResourceAccessValidator;
import com.bernardomg.security.springframework.test.auth.config.factory.Authentications;
import com.bernardomg.security.springframework.test.permission.config.factory.PermissionConstants;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringResourceAccessValidator")
class TestSpringResourceAccessValidator {

    @Mock
    private ResourcePermissionEvaluator   permissionEvaluator;

    @Mock
    private AuthenticationTrustResolver   trustResolver;

    @InjectMocks
    private SpringResourceAccessValidator validator;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("An authorized authenticated user is authorized")
    void testIsAuthorized() {
        final boolean        authorized;
        final Authentication authentication;

        // GIVEN
        authentication = Authentications.authenticatedWithCreateAuthorities();

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        Mockito.when(trustResolver.isAnonymous(authentication))
            .thenReturn(false);
        Mockito
            .when(permissionEvaluator.isAuthorized(authentication, PermissionConstants.DATA, PermissionConstants.READ))
            .thenReturn(true);

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.DATA, PermissionConstants.READ);

        // THEN
        Assertions.assertThat(authorized)
            .isTrue();
    }

    @Test
    @DisplayName("When the user is anonymous the user is not authorized")
    void testIsAuthorized_AnonymousAuthentication() {
        final Authentication authentication;
        final boolean        authorized;

        // GIVEN
        authentication = Authentications.authenticated();

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        Mockito.when(trustResolver.isAnonymous(authentication))
            .thenReturn(true);

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.DATA, PermissionConstants.READ);

        // THEN
        Assertions.assertThat(authorized)
            .isFalse();
    }

    @Test
    @DisplayName("When the evaluator rejects the permission the user is not authorized")
    void testIsAuthorized_EvaluatorRejectsPermission() {
        final Authentication authentication;
        final boolean        authorized;

        // GIVEN
        authentication = Authentications.authenticatedWithCreateAuthorities();

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        Mockito.when(trustResolver.isAnonymous(authentication))
            .thenReturn(false);
        Mockito
            .when(permissionEvaluator.isAuthorized(authentication, PermissionConstants.DATA, PermissionConstants.READ))
            .thenReturn(false);

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.DATA, PermissionConstants.READ);

        // THEN
        Assertions.assertThat(authorized)
            .isFalse();
    }

    @Test
    @DisplayName("When authentication is missing the user is not authorized")
    void testIsAuthorized_MissingAuthentication() {
        final boolean authorized;

        // GIVEN
        SecurityContextHolder.clearContext();

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.DATA, PermissionConstants.READ);

        // THEN
        Assertions.assertThat(authorized)
            .isFalse();
    }

    @Test
    @DisplayName("A non-anonymous authenticated user is evaluated")
    void testIsAuthorized_NonAnonymousAuthentication() {
        final Authentication authentication;
        final boolean        authorized;

        // GIVEN
        authentication = Authentications.authenticated();

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        Mockito.when(trustResolver.isAnonymous(authentication))
            .thenReturn(false);
        Mockito
            .when(permissionEvaluator.isAuthorized(authentication, PermissionConstants.DATA, PermissionConstants.READ))
            .thenReturn(true);

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.DATA, PermissionConstants.READ);

        // THEN
        Assertions.assertThat(authorized)
            .isTrue();

        Mockito.verify(permissionEvaluator)
            .isAuthorized(authentication, PermissionConstants.DATA, PermissionConstants.READ);
    }

    @Test
    @DisplayName("When the user is not authenticated the user is not authorized")
    void testIsAuthorized_NotAuthenticated() {
        final Authentication authentication;
        final boolean        authorized;

        // GIVEN
        authentication = Authentications.notAuthenticated();

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.DATA, PermissionConstants.READ);

        // THEN
        Assertions.assertThat(authorized)
            .isFalse();
    }

    @Test
    @DisplayName("A null action is rejected")
    void testIsAuthorized_NullAction() {
        Assertions.assertThatNullPointerException()
            .isThrownBy(() -> validator.isAuthorized(PermissionConstants.DATA, null))
            .withMessage("The action must not be null");

        verifyNoInteractions(permissionEvaluator, trustResolver);
    }

    @Test
    @DisplayName("A null resource is rejected")
    void testIsAuthorized_NullResource() {
        Assertions.assertThatNullPointerException()
            .isThrownBy(() -> validator.isAuthorized(null, PermissionConstants.READ))
            .withMessage("The resource must not be null");

        verifyNoInteractions(permissionEvaluator, trustResolver);
    }

}
