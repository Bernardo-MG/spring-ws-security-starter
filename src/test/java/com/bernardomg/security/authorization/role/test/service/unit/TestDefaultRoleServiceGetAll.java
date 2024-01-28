
package com.bernardomg.security.authorization.role.test.service.unit;

import static org.mockito.BDDMockito.given;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.model.request.RoleQuery;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.security.authorization.role.test.config.factory.RolesQuery;
import com.bernardomg.security.authorization.role.usecase.service.DefaultRoleService;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultRoleService - get all")
class TestDefaultRoleServiceGetAll {

    @Mock
    private RoleRepository     roleRepository;

    @InjectMocks
    private DefaultRoleService service;

    @Test
    @DisplayName("When there are roles they are returned")
    void testGetAll() {
        final Iterable<Role> roles;
        final RoleQuery      sample;
        final Pageable       pageable;
        final Iterable<Role> existing;

        // GIVEN
        pageable = Pageable.unpaged();

        sample = RolesQuery.empty();

        existing = List.of(Roles.valid());
        given(roleRepository.findAll(sample, pageable)).willReturn(existing);

        // WHEN
        roles = service.getAll(sample, pageable);

        // THEN
        Assertions.assertThat(roles)
            .containsExactly(Roles.valid());
    }

    @Test
    @DisplayName("When there are no roles nothing is returned")
    void testGetAll_NoData() {
        final Iterable<Role> roles;
        final RoleQuery      sample;
        final Pageable       pageable;
        final Iterable<Role> existing;

        // GIVEN
        pageable = Pageable.unpaged();

        sample = RolesQuery.empty();

        existing = List.of();
        given(roleRepository.findAll(sample, pageable)).willReturn(existing);

        // WHEN
        roles = service.getAll(sample, pageable);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

}
