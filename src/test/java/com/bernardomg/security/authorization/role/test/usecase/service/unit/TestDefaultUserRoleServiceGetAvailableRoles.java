
package com.bernardomg.security.authorization.role.test.usecase.service.unit;

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

import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authorization.role.domain.model.Role;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.security.authorization.role.usecase.service.DefaultUserRoleService;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultUserRoleService - get available roles")
class TestDefaultUserRoleServiceGetAvailableRoles {

    @Mock
    private RoleRepository         roleRepository;

    @InjectMocks
    private DefaultUserRoleService service;

    @Mock
    private UserRepository         userRepository;

    public TestDefaultUserRoleServiceGetAvailableRoles() {
        super();
    }

    @Test
    @DisplayName("When the user has available ones they are returned")
    void testGetAvailableRoles() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        given(roleRepository.findAvailableToUser(UserConstants.USERNAME, pageable)).willReturn(List.of(Roles.valid()));

        // WHEN
        roles = service.getAvailableRoles(UserConstants.USERNAME, pageable);

        // THEN
        Assertions.assertThat(roles)
            .containsExactly(Roles.valid());
    }

    @Test
    @DisplayName("When the user has no available ones nothing is returned")
    void testGetAvailableRoles_NoData() {
        final Iterable<Role> roles;
        final Pageable       pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        given(roleRepository.findAvailableToUser(UserConstants.USERNAME, pageable)).willReturn(List.of());

        // WHEN
        roles = service.getAvailableRoles(UserConstants.USERNAME, pageable);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

}
