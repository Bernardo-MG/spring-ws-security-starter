
package com.bernardomg.security.authorization.role.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.model.request.RoleQuery;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.test.config.annotation.SingleRole;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.security.authorization.role.test.config.factory.RolesQuery;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
@DisplayName("RoleRepository - find all by sample - filter")
class ITRoleRepositoryFindAllBySampleFilter {

    @Autowired
    private RoleRepository repository;

    public ITRoleRepositoryFindAllBySampleFilter() {
        super();
    }

    @Test
    @DisplayName("When filtering by name the correct role is returned")
    @SingleRole
    void testFindAll_FilterByName() {
        final Iterable<Role> roles;
        final RoleQuery      sample;
        final Pageable       pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        sample = RolesQuery.byName();

        // WHEN
        roles = repository.findAll(sample, pageable);

        // THEN
        Assertions.assertThat(roles)
            .containsExactly(Roles.valid());
    }

    @Test
    @DisplayName("When filtering by a not existing name nothing is returned")
    @SingleRole
    void testFindAll_FilterByName_NotExiting() {
        final Iterable<Role> roles;
        final RoleQuery      sample;
        final Pageable       pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        sample = RolesQuery.byNameNotExisting();

        // WHEN
        roles = repository.findAll(sample, pageable);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("When filtering by name and there is no data nothing is returned")
    void testFindAll_NoData() {
        final Iterable<Role> roles;
        final RoleQuery      sample;
        final Pageable       pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        sample = RolesQuery.byName();

        // WHEN
        roles = repository.findAll(sample, pageable);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

}
