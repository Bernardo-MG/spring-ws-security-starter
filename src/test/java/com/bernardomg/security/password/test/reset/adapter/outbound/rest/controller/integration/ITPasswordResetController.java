
package com.bernardomg.security.password.test.reset.adapter.outbound.rest.controller.integration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.password.reset.adapter.outbound.rest.controller.PasswordResetController;
import com.bernardomg.security.password.reset.adapter.outbound.rest.model.PasswordReset;
import com.bernardomg.security.password.reset.usecase.service.PasswordResetService;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.token.domain.model.UserTokenStatus;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.MvcIntegrationTest;
import com.bernardomg.test.json.JsonUtils;
import com.bernardomg.ws.response.domain.model.Response;

@MvcIntegrationTest
@ComponentScan(basePackageClasses = PasswordResetController.class)
@AllAuthoritiesMockUser
@DisplayName("PasswordResetController")
class ITPasswordResetController {

    @Autowired
    private MockMvc              mockMvc;

    @MockitoBean
    private PasswordResetService service;

    @Test
    @DisplayName("Can change password")
    void testChangePassword() throws Exception {
        final PasswordReset changeRequest;
        final ResultActions resultActions;

        // GIVEN
        changeRequest = new PasswordReset(UserConstants.NEW_PASSWORD);

        // WHEN
        resultActions = mockMvc.perform(post("/password/reset/{token}", Tokens.TOKEN).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(changeRequest)));

        // THEN
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("When changing the password, the data is sent to the service")
    void testChangePassword_CallsService() throws Exception {
        final PasswordReset changeRequest;

        // GIVEN
        changeRequest = new PasswordReset(UserConstants.NEW_PASSWORD);

        // WHEN
        mockMvc.perform(post("/password/reset/{token}", Tokens.TOKEN).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(changeRequest)));

        // THEN
        verify(service).changePassword(Tokens.TOKEN, UserConstants.NEW_PASSWORD);
    }

    @Test
    @DisplayName("Can validate the token")
    void testValidateToken() throws Exception {
        final UserTokenStatus tokenStatus;
        final ResultActions   resultActions;

        // GIVEN
        tokenStatus = new UserTokenStatus(UserConstants.USERNAME, true);
        given(service.validateToken(anyString())).willReturn(tokenStatus);

        // WHEN
        resultActions = mockMvc.perform(get("/password/reset/{token}", Tokens.TOKEN).with(csrf())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // THEN
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(JsonUtils.toJson(new Response<>(tokenStatus))));
    }

    @Test
    @DisplayName("When validating the token, the data is sent to the service")
    void testValidateToken_CallsService() throws Exception {
        final UserTokenStatus tokenStatus = new UserTokenStatus(UserConstants.USERNAME, true);

        // GIVEN
        given(service.validateToken(anyString())).willReturn(tokenStatus);

        // WHEN
        mockMvc.perform(get("/password/reset/{token}", Tokens.TOKEN).with(csrf())
            .contentType(MediaType.APPLICATION_JSON));

        // THEN
        verify(service).validateToken(Tokens.TOKEN);
    }

}
