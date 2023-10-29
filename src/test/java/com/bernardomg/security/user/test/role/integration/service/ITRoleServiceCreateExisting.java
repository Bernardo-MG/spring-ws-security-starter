
package com.bernardomg.security.user.test.role.integration.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.bernardomg.security.user.model.Role;
import com.bernardomg.security.user.model.request.RoleCreate;
import com.bernardomg.security.user.persistence.repository.RoleRepository;
import com.bernardomg.security.user.service.RoleService;
import com.bernardomg.security.user.test.role.util.model.RolesCreate;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("Role service - create - existing")
@Sql({ "/db/queries/security/role/single.sql" })
class ITRoleServiceCreateExisting {

    @Autowired
    private RoleRepository repository;

    @Autowired
    private RoleService    service;

    public ITRoleServiceCreateExisting() {
        super();
    }

    @Test
    @DisplayName("Doesn't create over existing ids")
    void testCreate() {
        final RoleCreate data;
        final Role       result;

        data = RolesCreate.valid();

        result = service.create(data);

        Assertions.assertThat(result.getId())
            .isNotEqualTo(1);
    }

    @Test
    @DisplayName("Doesn't create over existing ids")
    void testCreate_AddsEntity() {
        final RoleCreate data;

        data = RolesCreate.valid();

        service.create(data);

        Assertions.assertThat(repository.count())
            .isEqualTo(2);
    }

}
