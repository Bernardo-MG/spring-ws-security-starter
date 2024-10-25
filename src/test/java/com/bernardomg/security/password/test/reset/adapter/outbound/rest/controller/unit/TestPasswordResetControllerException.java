
package com.bernardomg.security.password.test.reset.adapter.outbound.rest.controller.unit;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.password.reset.adapter.outbound.rest.controller.PasswordResetController;
import com.bernardomg.security.password.reset.adapter.outbound.rest.controller.PasswordResetExceptionHandler;
import com.bernardomg.security.password.reset.adapter.outbound.rest.model.PasswordReset;
import com.bernardomg.security.password.reset.adapter.outbound.rest.model.PasswordResetChange;
import com.bernardomg.security.password.reset.usecase.service.PasswordResetService;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.test.json.JsonUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("PasswordResetController - Exceptions")
class TestPasswordResetControllerException {

    @InjectMocks
    private PasswordResetController controller;

    private MockMvc                 mockMvc;

    @Mock
    private PasswordResetService    service;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new PasswordResetExceptionHandler())
            .build();
    }

    @Test
    @DisplayName("When changing the password, if the service throws an exception this is hidden")
    void testChangePassword_ServiceThrowsException() throws Exception {
        final PasswordResetChange changeRequest;

        // GIVEN
        changeRequest = new PasswordResetChange(UserConstants.NEW_PASSWORD);

        // GIVEN
        willThrow(new RuntimeException("Service exception")).given(service)
            .changePassword(anyString(), anyString());

        // WHEN + THEN
        mockMvc.perform(post("/password/reset/{token}", Tokens.TOKEN).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(changeRequest)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("When starting the password reset, if the service throws an exception this is hidden")
    void testStartPasswordReset_ServiceThrowsException() throws Exception {
        final PasswordReset resetRequest = new PasswordReset(UserConstants.EMAIL);

        // GIVEN
        willThrow(new RuntimeException("Service exception")).given(service)
            .startPasswordReset(anyString());

        // WHEN + THEN
        mockMvc.perform(post("/password/reset").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(resetRequest)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("When validating the token, if the service throws an exception this is hidden")
    void testValidateToken_ServiceThrowsException() throws Exception {

        // GIVEN
        given(service.validateToken(anyString())).willThrow(new RuntimeException("Service exception"));

        // WHEN + THEN
        mockMvc.perform(get("/password/reset/{token}", Tokens.TOKEN).with(csrf())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

}
