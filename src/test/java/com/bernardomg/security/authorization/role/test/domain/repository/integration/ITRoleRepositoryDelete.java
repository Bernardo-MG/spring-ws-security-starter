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

package com.bernardomg.security.authorization.role.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleSpringRepository;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.test.config.annotation.RoleWithPermission;
import com.bernardomg.security.authorization.role.test.config.annotation.RoleWithoutPermissions;
import com.bernardomg.security.authorization.role.test.config.factory.RoleConstants;
import com.bernardomg.security.permission.adapter.inbound.jpa.repository.ResourcePermissionSpringRepository;
import com.bernardomg.security.user.data.adapter.inbound.jpa.repository.UserSpringRepository;
import com.bernardomg.security.user.test.config.annotation.EnabledUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("RoleRepository - delete")
class ITRoleRepositoryDelete {

    @Autowired
    private RoleRepository                     repository;

    @Autowired
    private ResourcePermissionSpringRepository resourcePermissionSpringRepository;

    @Autowired
    private RoleSpringRepository               springRepository;

    @Autowired
    private UserSpringRepository               userSpringRepository;

    public ITRoleRepositoryDelete() {
        super();
    }

    @Test
    @DisplayName("Deletes a role with no permissions")
    @RoleWithoutPermissions
    void testDelete() {
        // WHEN
        repository.delete(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(springRepository.count())
            .isZero();
    }

    @Test
    @DisplayName("When there is no data, nothing is removed")
    void testDelete_NoData() {
        // WHEN
        repository.delete(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(springRepository.count())
            .isZero();
    }

    @Test
    @DisplayName("Deletes a role with permissions")
    @RoleWithPermission
    void testDelete_WithPermissions() {
        // WHEN
        repository.delete(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(springRepository.count())
            .isZero();
    }

    @Test
    @DisplayName("When deleting a role, the permissions are not deleted")
    @RoleWithPermission
    void testDelete_WithPermissions_PermissionsNotDeleted() {
        // WHEN
        repository.delete(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(resourcePermissionSpringRepository.count())
            .isNotZero();
    }

    @Test
    @DisplayName("Deletes a role with user and permissions")
    @EnabledUser
    void testDelete_WithUser() {
        // WHEN
        repository.delete(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(springRepository.count())
            .isZero();
    }

    @Test
    @DisplayName("When deleting a role with user and permissions, the permissions are not deleted")
    @EnabledUser
    void testDelete_WithUser_PermissionsNotDeleted() {
        // WHEN
        repository.delete(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(resourcePermissionSpringRepository.count())
            .isNotZero();
    }

    @Test
    @DisplayName("When deleting a role with user and permissions, the user is not deleted")
    @EnabledUser
    void testDelete_WithUser_UserNotDeleted() {
        // WHEN
        repository.delete(RoleConstants.NAME);

        // THEN
        Assertions.assertThat(userSpringRepository.count())
            .isNotZero();
    }

}
