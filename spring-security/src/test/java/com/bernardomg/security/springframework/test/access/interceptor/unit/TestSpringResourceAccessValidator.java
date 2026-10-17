
package com.bernardomg.security.springframework.test.access.interceptor.unit;

import static org.mockito.Mockito.verifyNoInteractions;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
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
import com.bernardomg.security.springframework.access.interceptor.SecurityContextHolderResourceAccessValidator;
import com.bernardomg.security.springframework.test.permission.config.factory.PermissionConstants;

@ExtendWith(MockitoExtension.class)
@DisplayName("SecurityContextHolderResourceAccessValidator - is authorized")
class TestSpringResourceAccessValidator {

    @Mock
    private Authentication                               authentication;

    @Mock
    private ResourcePermissionEvaluator                  permissionEvaluator;

    @Mock
    private AuthenticationTrustResolver                  trustResolver;

    @InjectMocks
    private SecurityContextHolderResourceAccessValidator validator;

    @BeforeEach
    void loadAuthentication() {
        SecurityContextHolder.getContext()
            .setAuthentication(authentication);
    }

    @Test
    @DisplayName("When an authenticated user has permission, then access is authorized")
    void testIsAuthorized() {
        final boolean authorized;

        // GIVEN
        Mockito.when(trustResolver.isAuthenticated(authentication))
            .thenReturn(true);

        Mockito
            .when(permissionEvaluator.isAuthorized(authentication, PermissionConstants.DATA, PermissionConstants.READ))
            .thenReturn(true);

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.DATA, PermissionConstants.READ);

        // THEN
        Assertions.assertThat(authorized)
            .as("When an authenticated user has permission, then access is authorized (authorized)")
            .isTrue();
    }

    @Test
    @DisplayName("When the evaluator rejects the permission, then access is denied")
    void testIsAuthorized_EvaluatorRejectsPermission() {
        final boolean authorized;

        // GIVEN
        Mockito.when(trustResolver.isAuthenticated(authentication))
            .thenReturn(true);
        Mockito
            .when(permissionEvaluator.isAuthorized(authentication, PermissionConstants.DATA, PermissionConstants.READ))
            .thenReturn(false);

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.DATA, PermissionConstants.READ);

        // THEN
        Assertions.assertThat(authorized)
            .as("When the evaluator rejects the permission, then access is denied (authorized)")
            .isFalse();
    }

    @Test
    @DisplayName("When authentication is missing, then access is denied")
    void testIsAuthorized_MissingAuthentication() {
        final boolean authorized;

        // GIVEN
        SecurityContextHolder.clearContext();

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.DATA, PermissionConstants.READ);

        // THEN
        Assertions.assertThat(authorized)
            .as("When authentication is missing, then access is denied (authorized)")
            .isFalse();
    }

    @Test
    @DisplayName("When the user is not authenticated, then access is denied")
    void testIsAuthorized_NotAuthenticated() {
        final boolean authorized;

        // WHEN
        authorized = validator.isAuthorized(PermissionConstants.DATA, PermissionConstants.READ);

        // THEN
        Assertions.assertThat(authorized)
            .as("When the user is not authenticated, then access is denied (authorized)")
            .isFalse();
    }

    @Test
    @DisplayName("When the action is null, then it is rejected")
    void testIsAuthorized_NullAction() {
        final ThrowingCallable action;

        // WHEN
        action = () -> validator.isAuthorized(PermissionConstants.DATA, null);

        // THEN
        Assertions.assertThatNullPointerException()
            .as("null action is rejected")
            .isThrownBy(action)
            .withMessage("The action must not be null");

        verifyNoInteractions(permissionEvaluator, trustResolver);
    }

    @Test
    @DisplayName("When the resource is null, then it is rejected")
    void testIsAuthorized_NullResource() {
        final ThrowingCallable action;

        // WHEN
        action = () -> validator.isAuthorized(null, PermissionConstants.READ);

        // THEN
        Assertions.assertThatNullPointerException()
            .as("null resource is rejected")
            .isThrownBy(action)
            .withMessage("The resource must not be null");

        verifyNoInteractions(permissionEvaluator, trustResolver);
    }

}
