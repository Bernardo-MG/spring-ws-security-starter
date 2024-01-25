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

package com.bernardomg.security.initializer.adapter.inbound;

import com.bernardomg.security.authentication.user.domain.model.User;
import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.domain.repository.UserRoleRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Creates initial test users on app start. These are meant to help local development.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class UsersInitializer {

    private final RoleRepository     roleRepository;

    private final UserRepository     userRepository;

    private final UserRoleRepository userRoleRepository;

    public UsersInitializer(final UserRepository userRepo, final UserRoleRepository userRoleRepo,
            final RoleRepository roleRepo) {
        super();

        userRepository = userRepo;
        userRoleRepository = userRoleRepo;
        roleRepository = roleRepo;
    }

    public final void initialize() {
        log.debug("Initializing test users");

        runIfNotExists(this::initializeRootUser, "root");
        runIfNotExists(this::initializeReadUser, "read");

        log.debug("Initialized test users");
    }

    private final User getReadUser() {
        return User.builder()
            .withUsername("read")
            .withName("read")
            .withEmail("email2@nowhere.com")
            .withEnabled(true)
            .withLocked(false)
            .withExpired(false)
            .withPasswordExpired(false)
            .build();
    }

    private final User getRootUser() {
        return User.builder()
            .withUsername("root")
            .withName("root")
            .withEmail("email1@nowhere.com")
            .withEnabled(true)
            .withLocked(false)
            .withExpired(false)
            .withPasswordExpired(false)
            .build();
    }

    private final void initializeReadUser() {
        final User readUser;
        final User savedReadUser;
        final Role role;

        // Add read user
        readUser = getReadUser();
        savedReadUser = userRepository.save(readUser, "1234");

        role = roleRepository.findOne("READ")
            .get();

        userRoleRepository.save(savedReadUser.getUsername(), role.getName());
    }

    private final void initializeRootUser() {
        final User rootUser;
        final User savedRootUser;
        final Role role;

        // Add root user
        rootUser = getRootUser();
        savedRootUser = userRepository.save(rootUser, "1234");

        role = roleRepository.findOne("ADMIN")
            .get();

        userRoleRepository.save(savedRootUser.getUsername(), role.getName());
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
