
package com.bernardomg.security.password.test.reset.adapter.outbound.rest.controller.integration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bernardomg.security.password.reset.adapter.outbound.rest.controller.PasswordResetController;
import com.bernardomg.security.password.reset.adapter.outbound.rest.model.PasswordReset;
import com.bernardomg.security.password.reset.adapter.outbound.rest.model.PasswordResetChange;
import com.bernardomg.security.password.reset.usecase.service.PasswordResetService;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.token.domain.model.UserTokenStatus;
import com.bernardomg.test.config.annotation.MvcIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;

@MvcIntegrationTest
@WebMvcTest(PasswordResetController.class)
@Disabled
class ITPasswordResetController {

    @Autowired
    private MockMvc              mockMvc;

    @Autowired
    private ObjectMapper         objectMapper;

    @MockBean
    private PasswordResetService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testChangePassword() throws Exception {
        final PasswordResetChange changeRequest = new PasswordResetChange("newPassword");

        doNothing().when(service)
            .changePassword(anyString(), anyString());

        mockMvc.perform(post("/password/reset/{token}", "validToken").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(changeRequest)))
            .andExpect(status().isNoContent());
    }

    @Test
    void testStartPasswordReset() throws Exception {
        final PasswordReset resetRequest = new PasswordReset("test@example.com");

        doNothing().when(service)
            .startPasswordReset(anyString());

        mockMvc.perform(post("/password/reset").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(resetRequest)))
            .andExpect(status().isNoContent());
    }

    @Test
    void testValidateToken() throws Exception {
        final UserTokenStatus tokenStatus = UserTokenStatus.of(UserConstants.USERNAME, true);

        when(service.validateToken(anyString())).thenReturn(tokenStatus);

        mockMvc.perform(get("/password/reset/{token}", "validToken").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}
