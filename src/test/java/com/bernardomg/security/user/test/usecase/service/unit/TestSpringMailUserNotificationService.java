
package com.bernardomg.security.user.test.usecase.service.unit;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.bernardomg.security.jwt.test.configuration.Tokens;
import com.bernardomg.security.user.test.config.factory.Users;
import com.bernardomg.security.user.usecase.service.SpringMailUserNotificationService;
import com.bernardomg.security.user.usecase.service.UserNotificationService;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringMailUserNotificationService")
class TestSpringMailUserNotificationService {

    @Mock
    private JavaMailSender          javaMailSender;

    @Mock
    private MessageSource           messageSource;

    @Mock
    private SpringTemplateEngine    templateEng;

    private UserNotificationService userNotificationService;

    public TestSpringMailUserNotificationService() {
        super();
    }

    @BeforeEach
    private final void initializeSender() {
        userNotificationService = new SpringMailUserNotificationService(templateEng, javaMailSender,
            "sender@somewhere.com", "http://somewhere.com", "App", messageSource);
    }

    @Test
    @DisplayName("The message is sent")
    void testSendUserInvitation_MessageSent() throws Exception {
        // WHEN
        userNotificationService.sendUserInvitation(Users.enabled(), Tokens.TOKEN);

        // THEN
        verify(javaMailSender).send(ArgumentMatchers.any(MimeMessagePreparator.class));
    }

}
