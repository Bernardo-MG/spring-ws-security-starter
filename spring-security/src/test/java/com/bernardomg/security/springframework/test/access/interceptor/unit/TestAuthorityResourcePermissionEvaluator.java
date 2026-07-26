
package com.bernardomg.security.springframework.test.access.interceptor.unit;

import java.util.List;
import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.bernardomg.security.springframework.access.interceptor.AuthorityResourcePermissionEvaluator;
import com.bernardomg.security.springframework.model.ResourceActionGrantedAuthority;
import com.bernardomg.security.springframework.test.auth.config.factory.Authentications;
import com.bernardomg.security.springframework.test.permission.config.factory.PermissionConstants;

@DisplayName("AuthorityResourcePermissionEvaluator")
class TestAuthorityResourcePermissionEvaluator {

    private static final String                  ACTION   = PermissionConstants.CREATE;

    private static final String                  RESOURCE = PermissionConstants.DATA;

    private AuthorityResourcePermissionEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new AuthorityResourcePermissionEvaluator();
    }

    @Test
    @DisplayName("When the authentication has the permission, it is authorized")
    void testIsAuthorized() {
        final Authentication authentication;
        final boolean        authorized;

        // GIVEN
        authentication = Authentications.authenticatedWithCreateAuthorities();

        // WHEN
        authorized = evaluator.isAuthorized(authentication, RESOURCE, ACTION);

        // THEN
        Assertions.assertThat(authorized)
            .isTrue();
    }

    @Test
    @DisplayName("When the case is changed, it is authorized")
    void testIsAuthorized_CaseSensitive() {
        final ResourceActionGrantedAuthority authority;
        final Authentication                 authentication;

        // GIVEN
        authority = new ResourceActionGrantedAuthority(RESOURCE.toUpperCase(Locale.ROOT),
            ACTION.toUpperCase(Locale.ROOT));

        authentication = Authentications.withAuthorities(List.of(authority));

        // WHEN
        final boolean authorized = evaluator.isAuthorized(authentication, RESOURCE.toLowerCase(Locale.ROOT),
            ACTION.toLowerCase(Locale.ROOT));

        // THEN
        Assertions.assertThat(authorized)
            .isFalse();
    }

    @Test
    @DisplayName("When the authentication has no authorities, it is not authorized")
    void testIsAuthorized_NoAuthorities() {
        final Authentication authentication;
        final boolean        authorized;

        // GIVEN
        authentication = Authentications.authenticated();

        // WHEN
        authorized = evaluator.isAuthorized(authentication, RESOURCE, ACTION);

        // THEN
        Assertions.assertThat(authorized)
            .isFalse();
    }

    @Test
    @DisplayName("When no authority matches the action, it is not authorized")
    void testIsAuthorized_NotMatchingAction() {
        final Authentication authentication;
        final boolean        authorized;

        // GIVEN
        authentication = Authentications.authenticatedWithReadAuthorities();

        // WHEN
        authorized = evaluator.isAuthorized(authentication, RESOURCE, ACTION);

        // THEN
        Assertions.assertThat(authorized)
            .isFalse();
    }

    @Test
    @DisplayName("When no authority matches the resource, it is not authorized")
    void testIsAuthorized_NotMatchingResource() {
        final Authentication authentication;
        final boolean        authorized;

        // GIVEN
        authentication = Authentications.authenticatedWithAlternativeCreateAuthorities();

        // WHEN
        authorized = evaluator.isAuthorized(authentication, RESOURCE, ACTION);

        // THEN
        Assertions.assertThat(authorized)
            .isFalse();
    }

    @Test
    @DisplayName("A null authentication is rejected")
    void testIsAuthorized_NullAuthentication() {
        Assertions.assertThatNullPointerException()
            .isThrownBy(() -> evaluator.isAuthorized(null, RESOURCE, ACTION));
    }

    @Test
    @DisplayName("When the authentication has multiple authorities and one matches, it is authorized")
    void testIsAuthorized_OneAuthorityMatches() {
        final ResourceActionGrantedAuthority nonMatchingResource;
        final ResourceActionGrantedAuthority nonMatchingAction;
        final ResourceActionGrantedAuthority matching;
        final Authentication                 authentication;

        // GIVEN
        nonMatchingResource = new ResourceActionGrantedAuthority("alternative-resource", ACTION);
        nonMatchingAction = new ResourceActionGrantedAuthority(RESOURCE, "alternative-action");
        matching = new ResourceActionGrantedAuthority(RESOURCE, ACTION);

        authentication = Authentications.withAuthorities(
            List.of(nonMatchingResource, new SimpleGrantedAuthority("ROLE_USER"), nonMatchingAction, matching));

        // WHEN
        final boolean authorized = evaluator.isAuthorized(authentication, RESOURCE, ACTION);

        // THEN
        Assertions.assertThat(authorized)
            .isTrue();
    }

    @Test
    @DisplayName("When the authentication is not of the expected type, it is not authorized")
    void testIsAuthorized_SimpleAuthorities() {
        final Authentication authentication;
        final boolean        authorized;

        // GIVEN
        authentication = Authentications.authenticatedWithSimpleAuthorities();

        // WHEN
        authorized = evaluator.isAuthorized(authentication, RESOURCE, ACTION);

        // THEN
        Assertions.assertThat(authorized)
            .isFalse();
    }
}
