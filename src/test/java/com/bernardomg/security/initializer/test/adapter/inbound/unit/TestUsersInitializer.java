
package com.bernardomg.security.initializer.test.adapter.inbound.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.domain.repository.UserRoleRepository;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.security.initializer.adapter.inbound.UsersInitializer;
import com.bernardomg.security.login.domain.model.LoginRegister;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsersInitializer")
class TestUsersInitializer {

    @Captor
    private ArgumentCaptor<LoginRegister> loginRegisterCaptor;

    @Mock
    private RoleRepository                roleRepository;

    @Mock
    private UserRepository                userRepository;

    @Mock
    private UserRoleRepository            userRoleRepository;

    @InjectMocks
    private UsersInitializer              usersInitializer;

    @BeforeEach
    public final void initializeMocks() {
        given(roleRepository.save(ArgumentMatchers.any())).willReturn(Roles.valid());
    }

    @Test
    @DisplayName("Sets roles to users")
    void testRun_Permissions() {
        // WHEN
        usersInitializer.initialize();

        // THEN
        verify(userRoleRepository, atLeastOnce()).save(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Creates users")
    void testRun_Roles() {
        // WHEN
        usersInitializer.initialize();

        // THEN
        verify(userRepository, atLeastOnce()).save(ArgumentMatchers.any(), ArgumentMatchers.anyString());
    }

}
