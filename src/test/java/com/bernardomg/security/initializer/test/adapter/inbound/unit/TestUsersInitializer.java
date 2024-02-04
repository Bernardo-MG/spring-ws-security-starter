
package com.bernardomg.security.initializer.test.adapter.inbound.unit;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.initializer.adapter.inbound.UsersInitializer;
import com.bernardomg.security.initializer.usecase.service.UsersInitializerService;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsersInitializer")
class TestUsersInitializer {

    @InjectMocks
    private UsersInitializer        usersInitializer;

    @Mock
    private UsersInitializerService usersInitializerService;

    @Test
    @DisplayName("Calls service")
    void testInitialize() {
        // WHEN
        usersInitializer.initialize();

        // THEN
        verify(usersInitializerService).initialize();
    }

}
