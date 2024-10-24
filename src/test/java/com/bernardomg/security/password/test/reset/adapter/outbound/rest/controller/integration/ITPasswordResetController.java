
package com.bernardomg.security.password.test.reset.adapter.outbound.rest.controller.integration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.password.reset.adapter.outbound.rest.controller.PasswordResetController;
import com.bernardomg.security.password.reset.adapter.outbound.rest.model.PasswordReset;
import com.bernardomg.security.password.reset.adapter.outbound.rest.model.PasswordResetChange;
import com.bernardomg.security.password.reset.usecase.service.PasswordResetService;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.token.domain.model.UserTokenStatus;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.MvcIntegrationTest;
import com.bernardomg.test.json.JsonUtils;

@MvcIntegrationTest
@ComponentScan(basePackageClasses = PasswordResetController.class)
@DisplayName("PasswordResetController")
@AllAuthoritiesMockUser
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
        changeRequest = new PasswordResetChange(UserConstants.NEW_PASSWORD);

        // WHEN + THEN
        mockMvc.perform(post("/password/reset/{token}", Tokens.TOKEN).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(changeRequest)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("When changing the password, the data is sent to the service")
    void testChangePassword_CallsService() throws Exception {
        final PasswordResetChange changeRequest;

        // GIVEN
        changeRequest = new PasswordResetChange(UserConstants.NEW_PASSWORD);

        // WHEN
        mockMvc.perform(post("/password/reset/{token}", Tokens.TOKEN).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(changeRequest)));

        // THEN
        verify(service).changePassword(Tokens.TOKEN, UserConstants.NEW_PASSWORD);
    }

    @Test
    @DisplayName("Can start password reset")
    void testStartPasswordReset() throws Exception {
        final PasswordReset resetRequest;

        // GIVEN
        resetRequest = new PasswordReset(UserConstants.EMAIL);

        // WHEN + THEN
        mockMvc.perform(post("/password/reset").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(resetRequest)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("When starting the password reset, the data is sent to the service")
    void testStartPasswordReset_CallsService() throws Exception {
        final PasswordReset resetRequest;

        // GIVEN
        resetRequest = new PasswordReset(UserConstants.EMAIL);

        // WHEN
        mockMvc.perform(post("/password/reset").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(resetRequest)));

        // THEN
        verify(service).startPasswordReset(UserConstants.EMAIL);
    }

    @Test
    @DisplayName("Can validate the token")
    void testValidateToken() throws Exception {
        final boolean         valid       = true;
        final UserTokenStatus tokenStatus = UserTokenStatus.of(UserConstants.USERNAME, valid);

        // GIVEN
        given(service.validateToken(anyString())).willReturn(tokenStatus);

        // WHEN + THEN
        mockMvc.perform(get("/password/reset/{token}", Tokens.TOKEN).with(csrf())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content.username").value(UserConstants.USERNAME))
            .andExpect(jsonPath("$.content.valid").value(valid));
    }

    @Test
    @DisplayName("When validating the token, the data is sent to the service")
    void testValidateToken_CallsService() throws Exception {
        final UserTokenStatus tokenStatus = UserTokenStatus.of(UserConstants.USERNAME, true);

        // GIVEN
        given(service.validateToken(anyString())).willReturn(tokenStatus);

        // WHEN
        mockMvc.perform(get("/password/reset/{token}", Tokens.TOKEN).with(csrf())
            .contentType(MediaType.APPLICATION_JSON));

        // THEN
        verify(service).validateToken(Tokens.TOKEN);
    }

}
