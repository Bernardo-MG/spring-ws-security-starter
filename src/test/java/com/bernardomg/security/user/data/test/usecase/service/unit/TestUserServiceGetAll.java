
package com.bernardomg.security.user.data.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.user.data.domain.model.User;
import com.bernardomg.security.user.data.domain.model.UserQuery;
import com.bernardomg.security.user.data.domain.repository.UserRepository;
import com.bernardomg.security.user.data.usecase.service.DefaultUserService;
import com.bernardomg.security.user.notification.usecase.notificator.UserNotificator;
import com.bernardomg.security.user.test.config.factory.UserQueries;
import com.bernardomg.security.user.test.config.factory.Users;
import com.bernardomg.security.user.token.usecase.store.UserTokenStore;

@ExtendWith(MockitoExtension.class)
@DisplayName("User service - get all")
class TestUserServiceGetAll {

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

    public TestUserServiceGetAll() {
        super();
    }

    @Test
    @DisplayName("When there are users they are returned")
    void testGetAll() {
        final Iterable<User> users;
        final UserQuery      sample;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = UserQueries.empty();

        given(userRepository.findAll(sample, pagination, sorting)).willReturn(List.of(Users.enabled()));

        // WHEN
        users = service.getAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(users)
            .as("users")
            .containsExactly(Users.enabled());
    }

    @Test
    @DisplayName("When there are no users nothing is returned")
    void testGetAll_NoData() {
        final Iterable<User> users;
        final UserQuery      sample;
        final Pagination     pagination;
        final Sorting        sorting;

        // GIVEN
        pagination = new Pagination(1, 10);
        sorting = Sorting.unsorted();

        sample = UserQueries.empty();

        given(userRepository.findAll(sample, pagination, sorting)).willReturn(List.of());

        // WHEN
        users = service.getAll(sample, pagination, sorting);

        // THEN
        Assertions.assertThat(users)
            .as("users")
            .isEmpty();
    }

}
