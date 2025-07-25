
package com.bernardomg.security.role.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.test.config.factory.Roles;
import com.bernardomg.security.user.data.domain.exception.MissingUsernameException;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.permission.domain.repository.UserRoleRepository;
import com.bernardomg.security.user.permission.usecase.service.DefaultUserRoleService;
import com.bernardomg.security.user.test.config.factory.UserConstants;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultUserRoleService - get available roles")
class TestDefaultUserRoleServiceGetAvailableRoles {

    @InjectMocks
    private DefaultUserRoleService service;

    @Mock
    private UserRepository         userRepository;

    @Mock
    private UserRoleRepository     userRoleRepository;

    public TestDefaultUserRoleServiceGetAvailableRoles() {
        super();
    }

    @Test
    @DisplayName("When the user has available ones they are returned")
    void testGetAvailableRoles() {
        final Iterable<Role> roles;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        given(userRoleRepository.findAvailableToUser(UserConstants.USERNAME, pagination, sorting))
            .willReturn(List.of(Roles.withoutPermissions()));
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(true);

        // WHEN
        roles = service.getAvailableRoles(UserConstants.USERNAME, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .containsExactly(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("When the user has no available ones nothing is returned")
    void testGetAvailableRoles_NoData() {
        final Iterable<Role> roles;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        given(userRoleRepository.findAvailableToUser(UserConstants.USERNAME, pagination, sorting))
            .willReturn(List.of());
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(true);

        // WHEN
        roles = service.getAvailableRoles(UserConstants.USERNAME, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .isEmpty();
    }

    @Test
    @DisplayName("When the user doesn't exist an exception is thrown")
    void testGetAvailableRoles_NotExisting() {
        final ThrowingCallable execution;
        final Pagination       pagination;
        final Sorting          sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        given(userRepository.exists(UserConstants.USERNAME)).willReturn(false);

        // WHEN
        execution = () -> service.getAvailableRoles(UserConstants.USERNAME, pagination, sorting);

        // THEN
        Assertions.assertThatThrownBy(execution)
            .isInstanceOf(MissingUsernameException.class);
    }

}
