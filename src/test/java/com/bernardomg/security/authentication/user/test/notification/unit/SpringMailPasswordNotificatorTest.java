
package com.bernardomg.security.authentication.user.test.notification.unit;

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

import com.bernardomg.security.authentication.password.notification.PasswordNotificator;
import com.bernardomg.security.authentication.password.notification.SpringMailPasswordNotificator;
import com.bernardomg.security.authentication.user.test.config.factory.UserConstants;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringMailPasswordNotificator")
class SpringMailPasswordNotificatorTest {

    @Mock
    private JavaMailSender       javaMailSender;

    private PasswordNotificator  passwordNotificator;

    @Mock
    private SpringTemplateEngine templateEng;

    public SpringMailPasswordNotificatorTest() {
        super();
    }

    @BeforeEach
    private final void initializeSender() {
        passwordNotificator = new SpringMailPasswordNotificator(templateEng, javaMailSender, "sender@somewhere.com",
            "http://somewhere.com");
    }

    @Test
    @DisplayName("The message content is sent to the target email")
    void testSendEmail_Content() throws Exception {
        // WHEN
        passwordNotificator.sendPasswordRecoveryMessage(UserConstants.EMAIL, UserConstants.USERNAME, "token");

        // THEN
        verify(javaMailSender).send(ArgumentMatchers.any(MimeMessagePreparator.class));
    }

}
