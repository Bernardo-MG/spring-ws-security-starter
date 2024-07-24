
package com.bernardomg.security.password.test.reset.adapter.outbound.rest.controller.integration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.bernardomg.TestApplication;
import com.bernardomg.security.password.reset.adapter.outbound.rest.controller.PasswordResetController;
import com.bernardomg.security.password.reset.adapter.outbound.rest.model.PasswordReset;
import com.bernardomg.security.password.reset.adapter.outbound.rest.model.PasswordResetChange;
import com.bernardomg.security.password.reset.usecase.service.PasswordResetService;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.token.domain.model.UserTokenStatus;
import com.bernardomg.test.json.JsonUtils;

@ContextConfiguration(classes = TestApplication.class)
@WebMvcTest(controllers = PasswordResetController.class)
@DisplayName("PasswordResetController")
class ITPasswordResetController {

    @Autowired
    private MockMvc              mockMvc;

    @MockBean
    private PasswordResetService service;

    @Test
    @DisplayName("Can change password")
    void testChangePassword() throws Exception {
        final PasswordResetChange changeRequest;

        // GIVEN
        changeRequest = new PasswordResetChange("newPassword");

        // WHEN + THEN
        mockMvc.perform(post("/password/reset/{token}", "validToken").contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(changeRequest)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("When changing the password, if the service throws an exception this is hidden")
    void testChangePassword_ServiceThrowsException() throws Exception {
        final PasswordResetChange changeRequest;

        // GIVEN
        changeRequest = new PasswordResetChange("newPassword");

        // GIVEN
        willThrow(new RuntimeException("Service exception")).given(service)
            .changePassword(anyString(), anyString());

        // WHEN + THEN
        mockMvc.perform(post("/password/reset/{token}", "validToken").contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(changeRequest)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Can start password reset")
    void testStartPasswordReset() throws Exception {
        final PasswordReset resetRequest;

        // GIVEN
        resetRequest = new PasswordReset("test@example.com");

        // WHEN + THEN
        mockMvc.perform(post("/password/reset").contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(resetRequest)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("When starting the password reset, if the service throws an exception this is hidden")
    void testStartPasswordReset_ServiceThrowsException() throws Exception {
        final PasswordReset resetRequest = new PasswordReset("test@example.com");

        // GIVEN
        willThrow(new RuntimeException("Service exception")).given(service)
            .startPasswordReset(anyString());

        // WHEN + THEN
        mockMvc.perform(post("/password/reset").contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(resetRequest)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Can validate the token")
    void testValidateToken() throws Exception {
        final UserTokenStatus tokenStatus = UserTokenStatus.of(UserConstants.USERNAME, true);

        // GIVEN
        given(service.validateToken(anyString())).willReturn(tokenStatus);

        // WHEN + THEN
        mockMvc.perform(get("/password/reset/{token}", "validToken").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("When validating the token, if the service throws an exception this is hidden")
    void testValidateToken_ServiceThrowsException() throws Exception {

        // GIVEN
        given(service.validateToken(anyString())).willThrow(new RuntimeException("Service exception"));

        // WHEN + THEN
        mockMvc.perform(get("/password/reset/{token}", "validToken").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

}
