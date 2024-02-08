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

package com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository;

import java.util.Optional;

import org.springframework.data.domain.Example;

import com.bernardomg.security.authentication.user.adapter.inbound.jpa.model.UserEntity;
import com.bernardomg.security.authentication.user.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.RoleEntity;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.model.UserRoleEntity;
import com.bernardomg.security.authorization.role.domain.repository.UserRoleRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * User repository based on JPA entities.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Slf4j
public final class JpaUserRoleRepository implements UserRoleRepository {

    private final RoleSpringRepository     roleRepository;

    private final UserSpringRepository     userRepository;

    private final UserRoleSpringRepository userRoleRepository;

    public JpaUserRoleRepository(final UserSpringRepository userRepo, final RoleSpringRepository roleRepo,
            final UserRoleSpringRepository userRoleRepo) {
        super();

        userRepository = userRepo;
        roleRepository = roleRepo;
        userRoleRepository = userRoleRepo;
    }

    @Override
    public final void delete(final String username, final String role) {
        final UserRoleEntity       userRole;
        final Optional<RoleEntity> readRole;
        final Optional<UserEntity> readUser;

        readUser = userRepository.findOneByUsername(username);
        readRole = roleRepository.findOneByName(role);

        if ((readUser.isPresent()) && (readRole.isPresent())) {
            userRole = UserRoleEntity.builder()
                .withUserId(readUser.get()
                    .getId())
                .withRoleId(readRole.get()
                    .getId())
                .build();

            userRoleRepository.delete(userRole);
        } else {
            if (readUser.isEmpty()) {
                log.warn("User {} doesn't exist. Can't delete role", username);
            }
            if (readRole.isEmpty()) {
                log.warn("Role {} doesn't exist. Can't delete role for user {}", role, username);
            }
        }
    }

    @Override
    public final boolean existsForRole(final String role) {
        final UserRoleEntity       sample;
        final Optional<RoleEntity> roleEntity;
        final boolean              exists;

        // TODO: rename, it is not clear what this method is for

        roleEntity = roleRepository.findOneByName(role);
        if (roleEntity.isPresent()) {
            sample = UserRoleEntity.builder()
                .withRoleId(roleEntity.get()
                    .getId())
                .build();

            exists = userRoleRepository.exists(Example.of(sample));
        } else {
            exists = false;
        }

        return exists;
    }

    @Override
    public final void save(final String username, final String role) {
        final UserRoleEntity       userRole;
        final Optional<RoleEntity> readRole;
        final Optional<UserEntity> readUser;

        readUser = userRepository.findOneByUsername(username);
        readRole = roleRepository.findOneByName(role);

        if ((readUser.isPresent()) && (readRole.isPresent())) {
            userRole = UserRoleEntity.builder()
                .withUserId(readUser.get()
                    .getId())
                .withRoleId(readRole.get()
                    .getId())
                .build();

            userRoleRepository.save(userRole);
        } else {
            if (readUser.isEmpty()) {
                log.warn("User {} doesn't exist. Can't save role", username);
            }
            if (readRole.isEmpty()) {
                log.warn("Role {} doesn't exist. Can't save role for user {}", role, username);
            }
        }
    }

}
