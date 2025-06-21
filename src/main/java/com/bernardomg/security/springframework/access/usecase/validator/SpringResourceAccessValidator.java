/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2022-2023 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bernardomg.security.springframework.access.usecase.validator;

import java.util.Objects;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bernardomg.security.springframework.domain.model.ResourceActionGrantedAuthority;

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
    private static final Logger log = LoggerFactory.getLogger(SpringResourceAccessValidator.class);

    public SpringResourceAccessValidator() {
        super();
    }

    @Override
    public final boolean isAuthorized(final String resource, final String action) {
        final Authentication                            authentication;
        final boolean                                   authorized;
        final Predicate<ResourceActionGrantedAuthority> matchesPermission;

        Objects.requireNonNull(resource, "The resource must not be null");
        Objects.requireNonNull(action, "The action must not be null");

        authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        if (authentication == null) {
            log.debug("Missing authentication object");
            authorized = false;
        } else if (authentication.isAuthenticated()) {
            // Authenticated user

            // Curries the permission check
            matchesPermission = a -> matches(a, resource, action);

            // It is authorized if any authority matches
            authorized = authentication.getAuthorities()
                .stream()
                .filter(ResourceActionGrantedAuthority.class::isInstance)
                .map(ResourceActionGrantedAuthority.class::cast)
                .anyMatch(matchesPermission);
            log.debug("Authorized user {} against resource {} with action {}: {}", authentication.getName(), resource,
                action, authorized);
        } else {
            // Not authenticated user
            log.debug("User {} is not authenticated", authentication.getName());
            authorized = false;
        }

        return authorized;
    }

    /**
     * Checks if the authority matches the permission.
     *
     * @param authority
     *            authority to check
     * @param resource
     *            resource to validate
     * @param action
     *            action to validate
     * @return {@code true} if the authority matches the permission, {@code false} otherwise
     */
    private final boolean matches(final ResourceActionGrantedAuthority authority, final String resource,
            final String action) {
        return authority.getResource()
            .equals(resource)
                && authority.getAction()
                    .equals(action);
    }

}
