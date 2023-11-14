
package com.bernardomg.security.permission.test.integration.service;

import java.util.Collection;
import java.util.stream.StreamSupport;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import com.bernardomg.security.authorization.permission.model.ResourcePermission;
import com.bernardomg.security.authorization.permission.service.PermissionService;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@AllAuthoritiesMockUser
@DisplayName("Permission service - get all")
@Sql({ "/db/queries/security/action/crud.sql", "/db/queries/security/resource/single.sql",
        "/db/queries/security/permission/crud.sql" })
class ITPermissionServiceGetAll {

    @Autowired
    private PermissionService service;

    public ITPermissionServiceGetAll() {
        super();
    }

    @Test
    @DisplayName("Returns all the entities")
    void testGetAll_Count() {
        final Iterable<ResourcePermission> result;
        final Pageable                     pageable;

        pageable = Pageable.unpaged();

        result = service.getAll(pageable);

        Assertions.assertThat(result)
            .hasSize(4);
    }

    @Test
    @DisplayName("Returns all data")
    void testGetAll_Data() {
        final Iterable<ResourcePermission> data;
        final Pageable                     pageable;
        final Collection<String>           names;

        pageable = Pageable.unpaged();

        data = service.getAll(pageable);

        names = StreamSupport.stream(data.spliterator(), false)
            .map(p -> p.getResource() + ":" + p.getAction())
            .toList();

        Assertions.assertThat(names)
            .contains("DATA:CREATE")
            .contains("DATA:READ")
            .contains("DATA:UPDATE")
            .contains("DATA:DELETE");
    }

}
