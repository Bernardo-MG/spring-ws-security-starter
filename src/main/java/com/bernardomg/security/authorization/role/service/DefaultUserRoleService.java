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

package com.bernardomg.security.authorization.role.service;

import java.util.Objects;

import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.persistence.repository.UserRepository;
import com.bernardomg.security.authorization.role.model.DtoUserRole;
import com.bernardomg.security.authorization.role.model.Role;
import com.bernardomg.security.authorization.role.model.UserRole;
import com.bernardomg.security.authorization.role.persistence.model.UserRoleEntity;
import com.bernardomg.security.authorization.role.persistence.repository.RoleRepository;
import com.bernardomg.security.authorization.role.persistence.repository.UserRoleRepository;
import com.bernardomg.security.authorization.role.validation.AddUserRoleValidator;
import com.bernardomg.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DefaultUserRoleService implements UserRoleService {

    private final RoleRepository      roleRepository;

    private final UserRoleRepository  userRoleRepository;

    private final Validator<UserRole> validatorAddUserRole;

    private final Validator<UserRole> validatorRemoveUserRole;

    public DefaultUserRoleService(final UserRepository userRepo, final RoleRepository roleRepo,
            final UserRoleRepository userRoleRepo) {
        super();

        userRoleRepository = Objects.requireNonNull(userRoleRepo);
        roleRepository = Objects.requireNonNull(roleRepo);

        validatorAddUserRole = new AddUserRoleValidator(userRepo, roleRepo);
        validatorRemoveUserRole = new AddUserRoleValidator(userRepo, roleRepo);
    }

    @Override
    public final UserRole addRole(final long userId, final long roleId) {
        final UserRoleEntity userRoleSample;
        final UserRole       userRole;
        final UserRoleEntity created;

        log.debug("Adding role {} to user {}", roleId, userId);

        userRole = DtoUserRole.builder()
            .userId(userId)
            .roleId(roleId)
            .build();
        validatorAddUserRole.validate(userRole);

        userRoleSample = getUserRoleSample(userId, roleId);

        // Persist relationship
        created = userRoleRepository.save(userRoleSample);

        return toDto(created);
    }

    @Override
    public final Iterable<Role> getAvailableRoles(final long userId, final Pageable pageable) {
        return roleRepository.findAvailableToUser(userId, pageable);
    }

    @Override
    public final Iterable<Role> getRoles(final long userId, final Pageable pageable) {
        log.debug("Getting roles for user {} and pagination {}", userId, pageable);

        return roleRepository.findForUser(userId, pageable);
    }

    @Override
    public final UserRole removeRole(final long userId, final long roleId) {
        final UserRoleEntity userRoleSample;
        final UserRole       userRole;

        log.debug("Removing role {} from user {}", roleId, userId);

        userRole = DtoUserRole.builder()
            .userId(userId)
            .roleId(roleId)
            .build();
        validatorRemoveUserRole.validate(userRole);

        userRoleSample = getUserRoleSample(userId, roleId);

        // Persist relationship
        userRoleRepository.delete(userRoleSample);

        return toDto(userRoleSample);
    }

    private final UserRoleEntity getUserRoleSample(final long user, final long role) {
        return UserRoleEntity.builder()
            .userId(user)
            .roleId(role)
            .build();
    }

    private final UserRole toDto(final UserRoleEntity role) {
        return DtoUserRole.builder()
            .roleId(role.getRoleId())
            .userId(role.getUserId())
            .build();
    }

}
