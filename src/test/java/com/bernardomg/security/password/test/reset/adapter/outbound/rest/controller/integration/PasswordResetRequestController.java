
package com.bernardomg.security.password.test.reset.adapter.outbound.rest.controller.integration;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.bernardomg.security.password.reset.adapter.outbound.rest.controller.PasswordResetController;
import com.bernardomg.security.password.reset.adapter.outbound.rest.model.RequestPasswordReset;
import com.bernardomg.security.password.reset.usecase.service.PasswordResetService;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.MvcIntegrationTest;
import com.bernardomg.test.json.JsonUtils;

@MvcIntegrationTest
@ComponentScan(basePackageClasses = PasswordResetController.class)
@AllAuthoritiesMockUser
@DisplayName("PasswordResetRequestController")
class PasswordResetRequestController {

    @Autowired
    private MockMvc              mockMvc;

    @MockitoBean
    private PasswordResetService service;

    @Test
    @DisplayName("Can start password reset")
    void testStartPasswordReset() throws Exception {
        final RequestPasswordReset resetRequest;
        final ResultActions        resultActions;

        // GIVEN
        resetRequest = new RequestPasswordReset(UserConstants.EMAIL);

        // WHEN
        resultActions = mockMvc.perform(post("/password/reset").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(resetRequest)));

        // THEN
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("When starting the password reset, the data is sent to the service")
    void testStartPasswordReset_CallsService() throws Exception {
        final RequestPasswordReset resetRequest;

        // GIVEN
        resetRequest = new RequestPasswordReset(UserConstants.EMAIL);

        // WHEN
        mockMvc.perform(post("/password/reset").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(resetRequest)));

        // THEN
        verify(service).startPasswordReset(UserConstants.EMAIL);
    }

}
