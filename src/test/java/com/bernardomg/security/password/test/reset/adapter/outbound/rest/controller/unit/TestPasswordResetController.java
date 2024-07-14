
package com.bernardomg.security.password.test.reset.adapter.outbound.rest.controller.unit;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
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

import com.bernardomg.security.password.reset.adapter.outbound.rest.controller.PasswordResetController;
import com.bernardomg.security.password.reset.adapter.outbound.rest.model.PasswordReset;
import com.bernardomg.security.password.reset.adapter.outbound.rest.model.PasswordResetChange;
import com.bernardomg.security.password.reset.usecase.service.PasswordResetService;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.token.domain.model.UserTokenStatus;
import com.bernardomg.ws.error.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@DisplayName("PasswordResetController")
class TestPasswordResetController {

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @InjectMocks
    private PasswordResetController controller;

    private MockMvc                 mockMvc;

    @Mock
    private PasswordResetService    service;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    }

    @Test
    @DisplayName("Can change password")
    void testChangePassword() throws Exception {
        final PasswordResetChange changeRequest = new PasswordResetChange("newPassword");

        // WHEN + THEN
        mockMvc.perform(post("/password/reset/{token}", "validToken").contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(changeRequest)))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("When changing the password, if the service throws an exception an error is returned")
    void testChangePassword_ServiceThrowsException() throws Exception {
        final PasswordResetChange changeRequest = new PasswordResetChange("newPassword");

        // GIVEN
        doThrow(new RuntimeException("Service exception")).when(service)
            .changePassword(anyString(), anyString());

        // WHEN + THEN
        mockMvc.perform(post("/password/reset/{token}", "validToken").contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(changeRequest)))
            .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Can start password reset")
    void testStartPasswordReset() throws Exception {
        final PasswordReset resetRequest = new PasswordReset("test@example.com");

        // WHEN + THEN
        mockMvc.perform(post("/password/reset").contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(resetRequest)))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("When starting the password reset, if the service throws an exception an error is returned")
    void testStartPasswordReset_ServiceThrowsException() throws Exception {
        final PasswordReset resetRequest = new PasswordReset("test@example.com");

        // GIVEN
        doThrow(new RuntimeException("Service exception")).when(service)
            .startPasswordReset(anyString());

        // WHEN + THEN
        mockMvc.perform(post("/password/reset").contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(resetRequest)))
            .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Can validate the token")
    void testValidateToken() throws Exception {
        final UserTokenStatus tokenStatus = UserTokenStatus.of(UserConstants.USERNAME, true);

        // GIVEN
        when(service.validateToken(anyString())).thenReturn(tokenStatus);

        // WHEN + THEN
        mockMvc.perform(get("/password/reset/{token}", "validToken").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("When validating the token, if the service throws an exception an error is returned")
    void testValidateToken_ServiceThrowsException() throws Exception {
        when(service.validateToken(anyString())).thenThrow(new RuntimeException("Service exception"));

        // WHEN + THEN
        mockMvc.perform(get("/password/reset/{token}", "validToken").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());
    }

}