/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023-2025 the original author or authors.
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

package com.bernardomg.security.springframework.usecase.service;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bernardomg.security.domain.permission.model.ResourcePermission;
import com.bernardomg.security.domain.user.model.User;
import com.bernardomg.security.domain.user.repository.UserRepository;
import com.bernardomg.security.springframework.model.ResourceActionGrantedAuthority;

import jakarta.transaction.Transactional;

/**
 * User details service which takes the user data from the domain layer.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Transactional
public final class UserDomainDetailsService implements UserDetailsService {

    /**
     * Logger for the class.
     */
    private static final Logger  log          = LoggerFactory.getLogger(UserDomainDetailsService.class);

    private final Pattern        emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    /**
     * User repository.
     */
    private final UserRepository userRepository;

    /**
     * Constructs a user details service.
     *
     * @param userRepo
     *            users repository
     */
    public UserDomainDetailsService(final UserRepository userRepo) {
        super();

        userRepository = Objects.requireNonNull(userRepo, "Received a null pointer as user repository");
    }

    @Override
    public final UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User                                   user;
        final Collection<? extends GrantedAuthority> authorities;
        final UserDetails                            details;
        final String                                 password;
        final String                                 cleanedUsername;
        final Matcher                                emailMatcher;

        cleanedUsername = username.toLowerCase(Locale.ROOT);

        log.trace("Loading user {}", cleanedUsername);

        emailMatcher = emailPattern.matcher(username);
        if (emailMatcher.find()) {
            user = userRepository.findOneByEmail(cleanedUsername)
                .orElseThrow(() -> {
                    log.debug("Username {} not found in database", cleanedUsername);
                    throw new UsernameNotFoundException("Invalid username or credentials");
                });
        } else {
            user = userRepository.findOne(cleanedUsername)
                .orElseThrow(() -> {
                    log.debug("Username {} not found in database", cleanedUsername);
                    throw new UsernameNotFoundException("Invalid username or credentials");
                });
        }

        authorities = user.permissions()
            .stream()
            .map(this::toAuthority)
            .toList();

        if (authorities.isEmpty()) {
            log.debug("Username {} has no authorities", cleanedUsername);
            throw new UsernameNotFoundException("Invalid username or credentials");
        }

        password = userRepository.findPassword(user.username())
            .orElseThrow(() -> {
                log.debug("Username {} not found in database", cleanedUsername);
                throw new UsernameNotFoundException("Invalid username or credentials");
            });
        details = toUserDetails(user, password, authorities);

        log.debug("User {} exists. Enabled: {}. Non expired: {}. Non locked: {}. Credentials non expired: {}",
            cleanedUsername, details.isEnabled(), details.isAccountNonExpired(), details.isAccountNonLocked(),
            details.isCredentialsNonExpired());
        log.debug("Authorities for {}: {}", cleanedUsername, details.getAuthorities());

        log.trace("Loaded user {}", cleanedUsername);

        return details;
    }

    private final GrantedAuthority toAuthority(final ResourcePermission permission) {
        return new ResourceActionGrantedAuthority(permission.resource(), permission.action());
    }

    /**
     * Transforms a user into a user details object.
     *
     * @param user
     *            user to transform
     * @param password
     *            user password
     * @param authorities
     *            authorities for the user details
     * @return equivalent user details
     */
    private final UserDetails toUserDetails(final User user, final String password,
            final Collection<? extends GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.username(), password, user.enabled(),
            user.notExpired(), user.passwordNotExpired(), user.notLocked(), authorities);
    }

}
