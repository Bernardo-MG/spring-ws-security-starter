
package com.bernardomg.security.user.test.usecase.service.unit;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.user.test.config.factory.UserConstants;
import com.bernardomg.security.user.usecase.service.SpringMailUserNotificationService;
import com.bernardomg.security.user.usecase.service.UserNotificationService;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringMailUserNotificator")
class SpringMailUserNotificationServiceTest {

    @Mock
    private JavaMailSender          javaMailSender;

    @Mock
    private SpringTemplateEngine    templateEng;

    private UserNotificationService userNotificationService;

    public SpringMailUserNotificationServiceTest() {
        super();
    }

    @BeforeEach
    private final void initializeSender() {
        userNotificationService = new SpringMailUserNotificationService(templateEng, javaMailSender,
            "sender@somewhere.com", "http://somewhere.com");
    }

    @Test
    @DisplayName("The message is sent")
    void testSendEmail_MessageSent() throws Exception {
        // WHEN
        userNotificationService.sendUserInvitationMessage(UserConstants.EMAIL, UserConstants.USERNAME, Tokens.TOKEN);

        // THEN
        verify(javaMailSender).send(ArgumentMatchers.any(MimeMessagePreparator.class));
    }

}
