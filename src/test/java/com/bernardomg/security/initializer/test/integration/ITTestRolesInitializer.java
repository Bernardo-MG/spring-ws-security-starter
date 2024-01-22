
package com.bernardomg.security.initializer.test.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository.ResourcePermissionSpringRepository;
import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository.RolePermissionSpringRepository;
import com.bernardomg.security.authorization.permission.test.config.annotation.CrudPermissions;
import com.bernardomg.security.authorization.role.adapter.inbound.jpa.repository.RoleSpringRepository;
import com.bernardomg.security.initializer.TestRolesInitializer;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("TestRolesInitializer")
@CrudPermissions
class ITTestRolesInitializer {

    @Autowired
    private ResourcePermissionSpringRepository resourcePermissionRepository;

    @Autowired
    private RolePermissionSpringRepository     rolePermissionRepository;

    @Autowired
    private RoleSpringRepository               roleRepository;

    private TestRolesInitializer               testRolesInitializer;

    @BeforeEach
    public void initialize() {
        testRolesInitializer = new TestRolesInitializer(resourcePermissionRepository, roleRepository,
            rolePermissionRepository);
    }

    @Test
    @DisplayName("Sets permissions to roles")
    void testRun_Permissions() {
        final long count;

        testRolesInitializer.initialize();

        count = rolePermissionRepository.count();
        Assertions.assertThat(count)
            .isNotZero();
    }

    @Test
    @DisplayName("Creates roles")
    void testRun_Roles() {
        final long count;

        testRolesInitializer.initialize();

        count = roleRepository.count();
        Assertions.assertThat(count)
            .isEqualTo(2);
    }

}
