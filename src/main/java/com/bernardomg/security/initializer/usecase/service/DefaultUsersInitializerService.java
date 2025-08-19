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

package com.bernardomg.security.initializer.usecase.service;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.user.data.domain.model.User;
import com.bernardomg.security.user.data.domain.repository.UserRepository;

/**
 * Creates initial test users on app start. These are meant to help local development.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Transactional
public final class DefaultUsersInitializerService implements UsersInitializerService {

    /**
     * Logger for the class.
     */
    private static final Logger  log = LoggerFactory.getLogger(DefaultUsersInitializerService.class);

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    public DefaultUsersInitializerService(final UserRepository userRepo, final RoleRepository roleRepo) {
        super();

        userRepository = Objects.requireNonNull(userRepo);
        roleRepository = Objects.requireNonNull(roleRepo);
    }

    @Override
    public final void initialize() {
        log.debug("Initializing test users");

        runIfNotExists(this::initializeRootUser, "root");
        runIfNotExists(this::initializeReadUser, "read");

        log.debug("Initialized test users");
    }

    private final User getReadUser() {
        final Role role;

        role = roleRepository.findOne("READ")
            .get();

        return new User("email2@nowhere.com", "read", "read", true, true, true, true, List.of(role));
    }

    private final User getRootUser() {
        final Role role;

        role = roleRepository.findOne("ADMIN")
            .get();

        return new User("email1@nowhere.com", "root", "root", true, true, true, true, List.of(role));
    }

    private final void initializeReadUser() {
        final User readUser;

        // Add read user
        readUser = getReadUser();
        userRepository.saveNewUser(readUser);
        userRepository.resetPassword(readUser.username(), "1234");
    }

    private final void initializeRootUser() {
        final User rootUser;

        // Add root user
        rootUser = getRootUser();
        userRepository.saveNewUser(rootUser);
        userRepository.resetPassword(rootUser.username(), "1234");
    }

    private final void runIfNotExists(final Runnable runnable, final String name) {
        if (!userRepository.exists(name)) {
            runnable.run();
            log.debug("Initialized {} user", name);
        } else {
            log.debug("User {} already exists. Skipped initialization.", name);
        }
    }

}
