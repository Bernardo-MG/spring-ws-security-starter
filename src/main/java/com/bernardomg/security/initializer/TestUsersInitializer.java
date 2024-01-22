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

package com.bernardomg.security.initializer;

import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.UserRoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleSpringRepository;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.UserRoleSpringRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Creates initial test users on app start. These are meant to allow local development.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class TestUsersInitializer {

    /**
     * Password encoder.
     */
    private final PasswordEncoder          passwordEncoder;

    private final RoleSpringRepository     roleRepository;

    private final UserSpringRepository     userRepository;

    private final UserRoleSpringRepository userRoleRepository;

    public TestUsersInitializer(final UserSpringRepository userRepo, final UserRoleSpringRepository userRoleRepo,
            final RoleSpringRepository roleRepo, final PasswordEncoder passwordEnc) {
        super();

        userRepository = userRepo;
        userRoleRepository = userRoleRepo;
        roleRepository = roleRepo;
        passwordEncoder = passwordEnc;
    }

    public final void initialize() {
        log.debug("Initializing test users");

        runIfNotExists(this::initializeRootUser, "root");
        runIfNotExists(this::initializeReadUser, "read");

        log.debug("Initialized test users");
    }

    private final UserEntity getReadUser() {
        final String encodedPassword;

        encodedPassword = passwordEncoder.encode("1234");

        return UserEntity.builder()
            .withUsername("read")
            .withName("read")
            .withEmail("email2@nowhere.com")
            .withPassword(encodedPassword)
            .withEnabled(true)
            .withLocked(false)
            .withExpired(false)
            .withPasswordExpired(false)
            .build();
    }

    private final UserEntity getRootUser() {
        final String encodedPassword;

        encodedPassword = passwordEncoder.encode("1234");

        return UserEntity.builder()
            .withUsername("root")
            .withName("root")
            .withEmail("email1@nowhere.com")
            .withPassword(encodedPassword)
            .withEnabled(true)
            .withLocked(false)
            .withExpired(false)
            .withPasswordExpired(false)
            .build();
    }

    private final void initializeReadUser() {
        final UserEntity     readUser;
        final UserEntity     savedReadUser;
        final UserRoleEntity readUserRole;
        final RoleEntity     example;
        final RoleEntity     role;

        // Add read user
        readUser = getReadUser();
        savedReadUser = userRepository.save(readUser);

        example = RoleEntity.builder()
            .withName("READ")
            .build();
        role = roleRepository.findOne(Example.of(example))
            .get();

        readUserRole = UserRoleEntity.builder()
            .withUserId(savedReadUser.getId())
            .withRoleId(role.getId())
            .build();
        userRoleRepository.save(readUserRole);
    }

    private final void initializeRootUser() {
        final UserEntity     rootUser;
        final UserEntity     savedRootUser;
        final UserRoleEntity rootUserRole;
        final RoleEntity     example;
        final RoleEntity     role;

        // Add root user
        rootUser = getRootUser();
        savedRootUser = userRepository.save(rootUser);

        example = RoleEntity.builder()
            .withName("ADMIN")
            .build();
        role = roleRepository.findOne(Example.of(example))
            .get();

        rootUserRole = UserRoleEntity.builder()
            .withUserId(savedRootUser.getId())
            .withRoleId(role.getId())
            .build();
        userRoleRepository.save(rootUserRole);
    }

    private final void runIfNotExists(final Runnable runnable, final String name) {
        if (!userRepository.existsByUsername(name)) {
            runnable.run();
            log.debug("Initialized {} user", name);
        } else {
            log.debug("User {} already exists. Skipped initialization.", name);
        }
    }

}
