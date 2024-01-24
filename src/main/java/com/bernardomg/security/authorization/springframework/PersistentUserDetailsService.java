/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023 the original author or authors.
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

package com.bernardomg.security.authorization.springframework;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository.ResourcePermissionSpringRepository;
import com.bernardomg.security.authorization.permission.domain.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.domain.repository.ResourcePermissionRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * User details service which takes the user data from the persistence layer.
 * <h2>User names</h2>
 * <p>
 * Users are located through the username, with a case insensitive search. The persisted user details are expected to
 * contain the username in lower case, to avoid repeated usernames.
 * <h2>Granted authorities</h2>
 * <p>
 * Permissions will be acquired through a {@link ResourcePermissionSpringRepository}, which queries a permissions view.
 * This contains the resource and action pairs assigned to the user, and will be used to create the granted authorities.
 * <h2>Exceptions</h2>
 * <p>
 * When loading users any of these cases throws a {@code UsernameNotFoundException}:
 * <ul>
 * <li>There is no user for the username</li>
 * <li>There is a user, but he has no action</li>
 * </ul>
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class PersistentUserDetailsService implements UserDetailsService {

    /**
     * Resource permissions repository.
     */
    private final ResourcePermissionRepository resourcePermissionRepository;

    /**
     * User repository.
     */
    private final UserRepository               userRepository;

    /**
     * Constructs a user details service.
     *
     * @param userRepo
     *            users repository
     * @param resourcePermissionRepo
     *            resource permissions repository
     */
    public PersistentUserDetailsService(final UserRepository userRepo,
            final ResourcePermissionRepository resourcePermissionRepo) {
        super();

        userRepository = Objects.requireNonNull(userRepo, "Received a null pointer as user repository");
        resourcePermissionRepository = Objects.requireNonNull(resourcePermissionRepo,
            "Received a null pointer as resource permission repository");
    }

    @Override
    public final UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Optional<com.bernardomg.security.authentication.user.domain.model.User> user;
        final Collection<? extends GrantedAuthority>                                  authorities;
        final UserDetails                                                             details;
        final String                                                                  password;

        user = userRepository.findOne(username.toLowerCase(Locale.getDefault()));

        if (user.isEmpty()) {
            log.error("Username {} not found in database", username);
            throw new UsernameNotFoundException(String.format("Username %s not found in database", username));
        }

        authorities = resourcePermissionRepository.findAllForUser(user.get()
            .getUsername())
            .stream()
            .map(this::toAuthority)
            .toList();

        if (authorities.isEmpty()) {
            log.error("Username {} has no authorities", username);
            throw new UsernameNotFoundException(String.format("Username %s has no authorities", username));
        }

        password = userRepository.findPassword(username);
        details = toUserDetails(user.get(), password, authorities);

        log.debug("User {} exists. Enabled: {}. Non expired: {}. Non locked: {}. Credentials non expired: {}", username,
            details.isEnabled(), details.isAccountNonExpired(), details.isAccountNonLocked(),
            details.isCredentialsNonExpired());
        log.debug("Authorities for {}: {}", username, details.getAuthorities());

        return details;
    }

    private final GrantedAuthority toAuthority(final ResourcePermission permission) {
        return ResourceActionGrantedAuthority.builder()
            .withResource(permission.getResource())
            .withAction(permission.getAction())
            .build();
    }

    /**
     * Transforms a user entity into a user details object.
     *
     * @param user
     *            entity to transform
     * @param authorities
     *            authorities for the user details
     * @return equivalent user details
     */
    private final UserDetails toUserDetails(final User user, final String password,
            final Collection<? extends GrantedAuthority> authorities) {
        final Boolean enabled;
        final Boolean accountNonExpired;
        final Boolean credentialsNonExpired;
        final Boolean accountNonLocked;

        // Loads status
        enabled = user.isEnabled();
        accountNonExpired = !user.isExpired();
        credentialsNonExpired = !user.isPasswordExpired();
        accountNonLocked = !user.isLocked();

        return new org.springframework.security.core.userdetails.User(user.getUsername(), password, enabled,
            accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

}
