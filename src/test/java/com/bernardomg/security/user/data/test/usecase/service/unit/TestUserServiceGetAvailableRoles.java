
package com.bernardomg.security.user.data.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.data.domain.Page;
import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.role.domain.model.Role;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.role.test.config.factory.Roles;
import com.bernardomg.security.user.data.domain.exception.MissingUsernameException;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.data.domain.repository.UserRoleRepository;
import com.bernardomg.security.user.data.usecase.service.DefaultUserService;
import com.bernardomg.security.user.notification.usecase.notificator.UserNotificator;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.token.usecase.store.UserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultRoleService - get available roles")
class TestUserServiceGetAvailableRoles {

    @Mock
    private PasswordEncoder    passwordEncoder;

    @Mock
    private RoleRepository     roleRepository;

    @InjectMocks
    private DefaultUserService service;

    @Mock
    private UserTokenStore     tokenStore;

    @Mock
    private UserNotificator    userNotificator;

    @Mock
    private UserRepository     userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    public TestUserServiceGetAvailableRoles() {
        super();
    }

    @Test
    @DisplayName("When the user has available ones they are returned")
    void testGetAvailableRoles() {
        final Page<Role> roles;
        final Page<Role> existing;
        final Pagination pagination;
        final Sorting    sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        existing = new Page<>(List.of(Roles.withoutPermissions()), 0, 0, 0, 0, 0, false, false, sorting);
        given(userRoleRepository.findAvailableToUser(UserConstants.USERNAME, pagination, sorting)).willReturn(existing);
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(true);

        // WHEN
        roles = service.getAvailableRoles(UserConstants.USERNAME, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .containsExactly(Roles.withoutPermissions());
    }

    @Test
    @DisplayName("When the user has no available ones nothing is returned")
    void testGetAvailableRoles_NoData() {
        final Page<Role> roles;
        final Page<Role> existing;
        final Pagination pagination;
        final Sorting    sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        existing = new Page<>(List.of(), 0, 0, 0, 0, 0, false, false, sorting);
        given(userRoleRepository.findAvailableToUser(UserConstants.USERNAME, pagination, sorting)).willReturn(existing);
        given(userRepository.exists(UserConstants.USERNAME)).willReturn(true);

        // WHEN
        roles = service.getAvailableRoles(UserConstants.USERNAME, pagination, sorting);

        // THEN
        Assertions.assertThat(roles)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
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
