
package com.bernardomg.security.initializer.test.usecase.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bernardomg.security.initializer.usecase.service.DefaultUsersInitializerService;
import com.bernardomg.security.role.domain.repository.RoleRepository;
import com.bernardomg.security.role.test.config.factory.Roles;
import com.bernardomg.security.user.domain.repository.UserRepository;
import com.bernardomg.security.user.test.config.factory.Users;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsersInitializer")
class TestUsersInitializer {

    @Mock
    private PasswordEncoder                passwordEncoder;

    @Mock
    private RoleRepository                 roleRepository;

    @Mock
    private UserRepository                 userRepository;

    @InjectMocks
    private DefaultUsersInitializerService usersInitializerService;

    @BeforeEach
    public final void initializeMocks() {
        given(roleRepository.findOne(ArgumentMatchers.any())).willReturn(Optional.of(Roles.withoutPermissions()));
        given(userRepository.save(ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn(Users.enabled());
    }

    @Test
    @DisplayName("Creates users")
    void testInitialize_Roles() {
        // WHEN
        usersInitializerService.initialize();

        // THEN
        verify(userRepository, atLeastOnce()).save(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

}
