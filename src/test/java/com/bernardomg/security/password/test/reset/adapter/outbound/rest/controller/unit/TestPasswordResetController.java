
package com.bernardomg.security.password.test.reset.adapter.outbound.rest.controller.unit;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.password.reset.adapter.outbound.rest.controller.PasswordResetController;
import com.bernardomg.security.password.reset.adapter.outbound.rest.controller.PasswordResetExceptionHandler;
import com.bernardomg.security.password.reset.adapter.outbound.rest.model.PasswordReset;
import com.bernardomg.security.password.reset.adapter.outbound.rest.model.PasswordResetChange;
import com.bernardomg.security.password.reset.usecase.service.PasswordResetService;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.token.domain.model.UserTokenStatus;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.json.JsonUtils;

@RunWith(SpringRunner.class)
@WebMvcTest(PasswordResetController.class)
@DisplayName("PasswordResetController")
@AllAuthoritiesMockUser
class TestPasswordResetController {

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
        mockMvc.perform(post("/password/reset/{token}", Tokens.TOKEN)
            .with(csrf()).contentType(MediaType.APPLICATION_JSON)
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
        mockMvc.perform(post("/password/reset/{token}", Tokens.TOKEN)
            .with(csrf()).contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(changeRequest)));

        // THEN
        verify(service).changePassword(Tokens.TOKEN, UserConstants.NEW_PASSWORD);
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
        mockMvc.perform(post("/password/reset/{token}", Tokens.TOKEN)
            .with(csrf()).contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(changeRequest)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Can start password reset")
    void testStartPasswordReset() throws Exception {
        final PasswordReset resetRequest;

        // GIVEN
        resetRequest = new PasswordReset(UserConstants.EMAIL);

        // WHEN + THEN
        mockMvc.perform(post("/password/reset")
            .with(csrf()).contentType(MediaType.APPLICATION_JSON)
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
        mockMvc.perform(post("/password/reset")
            .with(csrf()).contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(resetRequest)));

        // THEN
        verify(service).startPasswordReset(UserConstants.EMAIL);
    }

    @Test
    @DisplayName("When starting the password reset, if the service throws an exception this is hidden")
    void testStartPasswordReset_ServiceThrowsException() throws Exception {
        final PasswordReset resetRequest = new PasswordReset(UserConstants.EMAIL);

        // GIVEN
        willThrow(new RuntimeException("Service exception")).given(service)
            .startPasswordReset(anyString());

        // WHEN + THEN
        mockMvc.perform(post("/password/reset")
            .with(csrf()).contentType(MediaType.APPLICATION_JSON)
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
        mockMvc.perform(get("/password/reset/{token}", Tokens.TOKEN)
            .with(csrf()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(JsonUtils.toJson(tokenStatus)));
    }

    @Test
    @DisplayName("When validating the token, the data is sent to the service")
    void testValidateToken_CallsService() throws Exception {
        final UserTokenStatus tokenStatus = UserTokenStatus.of(UserConstants.USERNAME, true);

        // GIVEN
        given(service.validateToken(anyString())).willReturn(tokenStatus);

        // WHEN
        mockMvc.perform(get("/password/reset/{token}", Tokens.TOKEN)
            .with(csrf()).contentType(MediaType.APPLICATION_JSON));

        // THEN
        verify(service).validateToken(Tokens.TOKEN);
    }

    @Test
    @DisplayName("When validating the token, if the service throws an exception this is hidden")
    void testValidateToken_ServiceThrowsException() throws Exception {

        // GIVEN
        given(service.validateToken(anyString())).willThrow(new RuntimeException("Service exception"));

        // WHEN + THEN
        mockMvc.perform(get("/password/reset/{token}", Tokens.TOKEN)
            .with(csrf()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

}
