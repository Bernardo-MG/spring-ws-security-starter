
package com.bernardomg.security.springframework.access.interceptor;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bernardomg.framework.security.access.interceptor.ResourceAccessValidator;
import com.bernardomg.security.springframework.model.ResourceActionGrantedAuthority;

/**
 * Validates permissions over a resource with the help of Spring. Permissions are checked through the user authorities,
 * concretely it will look for a {@link ResourceActionGrantedAuthority} matching the permission.
 * <p>
 * This security validation is applied against the user in session.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public final class SpringResourceAccessValidator implements ResourceAccessValidator {

    /**
     * Logger for the class.
     */
    private static final Logger               log = LoggerFactory.getLogger(SpringResourceAccessValidator.class);

    private final ResourcePermissionEvaluator permissionEvaluator;

    private final AuthenticationTrustResolver trustResolver;

    public SpringResourceAccessValidator(final ResourcePermissionEvaluator permissionEvaluator,
            final AuthenticationTrustResolver trustResolver) {

        this.permissionEvaluator = Objects.requireNonNull(permissionEvaluator,
            "The permission evaluator must not be null");
        this.trustResolver = Objects.requireNonNull(trustResolver, "The trust resolver must not be null");
    }

    @Override
    public final boolean isAuthorized(final String resource, final String action) {
        final Authentication authentication;
        final boolean        authorized;

        Objects.requireNonNull(resource, "The resource must not be null");
        Objects.requireNonNull(action, "The action must not be null");

        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        if (authentication == null) {
            // Not authenticated user
            log.debug("User is not authenticated");
            authorized = false;
        } else if (isAuthenticated(authentication)) {
            // Authenticated user

            authorized = permissionEvaluator.isAuthorized(authentication, resource, action);

            log.debug("Authorized user {} against resource {} with action {}: {}", authentication.getName(), resource,
                action, authorized);
        } else {
            // Not authenticated user
            log.debug("User {} is not authenticated", authentication.getName());
            authorized = false;
        }

        return authorized;
    }

    private boolean isAuthenticated(final Authentication authentication) {
        return authentication.isAuthenticated() && !trustResolver.isAnonymous(authentication);
    }

}
