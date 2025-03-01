
package com.bernardomg.security.password.test.reset.adapter.outbound.rest.controller.integration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.password.reset.adapter.outbound.rest.controller.PasswordResetController;
import com.bernardomg.security.password.reset.adapter.outbound.rest.controller.PasswordResetExceptionHandler;
import com.bernardomg.security.password.reset.adapter.outbound.rest.model.PasswordReset;
import com.bernardomg.security.password.reset.adapter.outbound.rest.model.PasswordResetChange;
import com.bernardomg.security.password.reset.usecase.service.PasswordResetService;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.MvcIntegrationTest;
import com.bernardomg.test.json.JsonUtils;

@MvcIntegrationTest
@ContextConfiguration(classes = { PasswordResetExceptionHandler.class })
@ComponentScan(basePackageClasses = PasswordResetController.class)
@AllAuthoritiesMockUser
@DisplayName("PasswordResetController - Exceptions")
class ITPasswordResetControllerException {

    @Autowired
    private MockMvc              mockMvc;

    @MockitoBean
    private PasswordResetService service;

    @Test
    @DisplayName("When changing the password, if the service throws an exception this is hidden")
    void testChangePassword_ServiceThrowsException() throws Exception {
        final PasswordResetChange changeRequest;
        final ResultActions       resultActions;

        // GIVEN
        changeRequest = new PasswordResetChange(UserConstants.NEW_PASSWORD);

        // GIVEN
        willThrow(new RuntimeException("Service exception")).given(service)
            .changePassword(anyString(), anyString());

        // WHEN
        resultActions = mockMvc.perform(post("/password/reset/{token}", Tokens.TOKEN).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(changeRequest)));

        // THEN
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("When starting the password reset, if the service throws an exception this is hidden")
    void testStartPasswordReset_ServiceThrowsException() throws Exception {
        final PasswordReset resetRequest;
        final ResultActions resultActions;

        resetRequest = new PasswordReset(UserConstants.EMAIL);

        // GIVEN
        willThrow(new RuntimeException("Service exception")).given(service)
            .startPasswordReset(anyString());

        // WHEN
        resultActions = mockMvc.perform(post("/password/reset").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(resetRequest)));

        // THEN
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("When validating the token, if the service throws an exception this is hidden")
    void testValidateToken_ServiceThrowsException() throws Exception {
        final ResultActions resultActions;

        // GIVEN
        given(service.validateToken(anyString())).willThrow(new RuntimeException("Service exception"));

        // WHEN
        resultActions = mockMvc.perform(get("/password/reset/{token}", Tokens.TOKEN).with(csrf())
            .contentType(MediaType.APPLICATION_JSON));

        // THEN
        resultActions.andExpect(status().isOk());
    }

}
