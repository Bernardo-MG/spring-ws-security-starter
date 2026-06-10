
package com.bernardomg.security.initializer.test.adapter.inbound.unit;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.security.initializer.adapter.inbound.RolesInitializer;
import com.bernardomg.security.initializer.usecase.service.RolesInitializerService;

@ExtendWith(MockitoExtension.class)
@DisplayName("RolesInitializer")
class TestRolesInitializer {

    @InjectMocks
    private RolesInitializer        rolesInitializer;

    @Mock
    private RolesInitializerService rolesInitializerService;

    @Test
    @DisplayName("Sets permissions to roles")
    void testInitialize() {
        // WHEN
        rolesInitializer.initialize();

        // THEN
        verify(rolesInitializerService).initialize();
    }

}
