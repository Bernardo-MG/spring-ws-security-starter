
package com.bernardomg.security.user.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
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
import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.user.domain.model.User;
import com.bernardomg.security.user.domain.model.UserQuery;
import com.bernardomg.security.user.domain.repository.UserRepository;
import com.bernardomg.security.user.domain.repository.UserRoleRepository;
import com.bernardomg.security.user.test.config.factory.UserQueries;
import com.bernardomg.security.user.test.config.factory.Users;
import com.bernardomg.security.user.usecase.service.DefaultUserService;
import com.bernardomg.security.user.usecase.store.UserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("User service - get all")
class TestUserServiceGetAll {

    @Mock
    private EventEmitter       eventEmitter;

    @Mock
    private PasswordEncoder    passwordEncoder;

    @Mock
    private RoleRepository     roleRepository;

    @InjectMocks
    private DefaultUserService service;

    @Mock
    private UserTokenStore     tokenStore;

    @Mock
    private UserRepository     userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    public TestUserServiceGetAll() {
        super();
    }

    @Test
    @DisplayName("When there are users they are returned")
    void testGetAll() {
        final Page<User> users;
        final Page<User> existing;
        final UserQuery  sample;
        final Pagination pagination;
        final Sorting    sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = UserQueries.empty();

        existing = new Page<>(List.of(Users.enabled()), 0, 0, 0, 0, 0, false, false, sorting);
        given(userRepository.findAll(sample, pagination, sorting)).willReturn(existing);

        // WHEN
        users = service.getAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(users)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("users")
            .containsExactly(Users.enabled());
    }

    @Test
    @DisplayName("When there are no users nothing is returned")
    void testGetAll_NoData() {
        final Page<User> users;
        final Page<User> existing;
        final UserQuery  sample;
        final Pagination pagination;
        final Sorting    sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = UserQueries.empty();

        existing = new Page<>(List.of(), 0, 0, 0, 0, 0, false, false, sorting);
        given(userRepository.findAll(sample, pagination, sorting)).willReturn(existing);

        // WHEN
        users = service.getAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(users)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("users")
            .isEmpty();
    }

}
