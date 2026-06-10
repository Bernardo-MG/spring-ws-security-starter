
package com.bernardomg.security.password.test.reset.adapter.outbound.rest.controller.integration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

import com.bernardomg.security.openapi.model.RequestPasswordResetDto;
import com.bernardomg.security.password.reset.adapter.outbound.rest.controller.PasswordResetRequestController;
import com.bernardomg.security.password.reset.usecase.service.PasswordResetService;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.test.TestApplication;
import com.bernardomg.test.config.annotation.AllAuthoritiesMockUser;
import com.bernardomg.test.config.annotation.MvcIntegrationTest;
import com.bernardomg.test.json.JsonUtils;

@MvcIntegrationTest
@ComponentScan(basePackageClasses = PasswordResetRequestController.class)
@AllAuthoritiesMockUser
@ContextConfiguration(classes = TestApplication.class)
@DisplayName("PasswordResetRequestController - Exceptions")
class ITPasswordResetRequestControllerException {

    @Autowired
    private MockMvc              mockMvc;

    @MockitoBean
    private PasswordResetService service;

    @Test
    @DisplayName("When starting the password reset, if the service throws an exception this is hidden")
    void testStartPasswordReset_ServiceThrowsException() throws Exception {
        final RequestPasswordResetDto resetRequest;
        final ResultActions           resultActions;

        resetRequest = new RequestPasswordResetDto(UserConstants.EMAIL);

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

}
