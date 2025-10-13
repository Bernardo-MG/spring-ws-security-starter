
package com.bernardomg.security.authentication.password.test.change.usecase.service.unit;

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
import com.bernardomg.security.password.change.usecase.service.PasswordNotificationService;
import com.bernardomg.security.password.change.usecase.service.SpringMailPasswordNotificationService;
import com.bernardomg.security.user.test.config.factory.Users;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringMailPasswordNotificationService")
class SpringMailPasswordNotificationServiceTest {

    @Mock
    private JavaMailSender              javaMailSender;

    private PasswordNotificationService passwordNotificator;

    @Mock
    private SpringTemplateEngine        templateEng;

    public SpringMailPasswordNotificationServiceTest() {
        super();
    }

    @BeforeEach
    private final void initializeSender() {
        passwordNotificator = new SpringMailPasswordNotificationService(templateEng, javaMailSender,
            "sender@somewhere.com", "http://somewhere.com", "App");
    }

    @Test
    @DisplayName("The message content is sent to the target email")
    void testSendEmail_Content() throws Exception {
        // WHEN
        passwordNotificator.sendPasswordRecoveryMessage(Users.enabled(), Tokens.TOKEN);

        // THEN
        verify(javaMailSender).send(ArgumentMatchers.any(MimeMessagePreparator.class));
    }

}
