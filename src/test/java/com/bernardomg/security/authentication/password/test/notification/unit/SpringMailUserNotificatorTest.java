
package com.bernardomg.security.authentication.password.test.notification.unit;

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

import com.bernardomg.security.authentication.user.adapter.outbound.email.SpringMailUserNotificator;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;
import com.bernardomg.security.authentication.user.usecase.notification.UserNotificator;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringMailUserNotificator")
class SpringMailUserNotificatorTest {

    @Mock
    private JavaMailSender       javaMailSender;

    @Mock
    private SpringTemplateEngine templateEng;

    private UserNotificator      userNotificator;

    public SpringMailUserNotificatorTest() {
        super();
    }

    @BeforeEach
    private final void initializeSender() {
        userNotificator = new SpringMailUserNotificator(templateEng, javaMailSender, "sender@somewhere.com",
            "http://somewhere.com");
    }

    @Test
    @DisplayName("The message is sent")
    void testSendEmail_MessageSent() throws Exception {
        // WHEN
        userNotificator.sendUserRegisteredMessage(UserConstants.EMAIL, UserConstants.USERNAME, "token");

        // THEN
        verify(javaMailSender).send(ArgumentMatchers.any(MimeMessagePreparator.class));
    }

}
