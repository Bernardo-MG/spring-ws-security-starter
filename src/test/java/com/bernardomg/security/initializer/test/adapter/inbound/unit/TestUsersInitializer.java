
package com.bernardomg.security.initializer.test.adapter.inbound.unit;

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

import com.bernardomg.security.authentication.user.domain.repository.UserRepository;
import com.bernardomg.security.authentication.user.test.config.factory.Users;
import com.bernardomg.security.authorization.role.domain.repository.RoleRepository;
import com.bernardomg.security.authorization.role.domain.repository.UserRoleRepository;
import com.bernardomg.security.authorization.role.test.config.factory.Roles;
import com.bernardomg.security.initializer.adapter.inbound.UsersInitializer;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsersInitializer")
class TestUsersInitializer {

    @Mock
    private RoleRepository     roleRepository;

    @Mock
    private UserRepository     userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private UsersInitializer   usersInitializer;

    @BeforeEach
    public final void initializeMocks() {
        given(roleRepository.findOne(ArgumentMatchers.any())).willReturn(Optional.of(Roles.valid()));
        given(userRepository.save(ArgumentMatchers.any(), ArgumentMatchers.anyString())).willReturn(Users.enabled());
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
