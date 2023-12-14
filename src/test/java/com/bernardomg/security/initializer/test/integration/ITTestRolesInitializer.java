
package com.bernardomg.security.initializer.test.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;

import com.bernardomg.security.authorization.permission.persistence.repository.ResourcePermissionRepository;
import com.bernardomg.security.authorization.permission.persistence.repository.RolePermissionRepository;
import com.bernardomg.security.authorization.permission.test.config.CrudPermissions;
import com.bernardomg.security.authorization.role.persistence.repository.RoleRepository;
import com.bernardomg.security.initializer.TestRolesInitializer;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("TestRolesInitializer")
@CrudPermissions
class ITTestRolesInitializer {

    @Autowired
    private ResourcePermissionRepository resourcePermissionRepository;

    @Autowired
    private RolePermissionRepository     rolePermissionRepository;

    @Autowired
    private RoleRepository               roleRepository;

    private TestRolesInitializer         testRolesInitializer;

    @BeforeEach
    public void initialize() {
        testRolesInitializer = new TestRolesInitializer(resourcePermissionRepository, roleRepository,
            rolePermissionRepository);
    }

    @Test
    @DisplayName("Sets permissions to roles")
    void testRun_Permissions() throws Exception {
        final long count;

        testRolesInitializer.run(new DefaultApplicationArguments());

        count = rolePermissionRepository.count();
        Assertions.assertThat(count)
            .isNotZero();
    }

    @Test
    @DisplayName("Creates roles")
    void testRun_Roles() throws Exception {
        final long count;

        testRolesInitializer.run(new DefaultApplicationArguments());

        count = roleRepository.count();
        Assertions.assertThat(count)
            .isEqualTo(2);
    }

}
