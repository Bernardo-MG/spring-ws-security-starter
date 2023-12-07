
package com.bernardomg.security.authentication.password.notification.unit;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.bernardomg.security.authentication.user.notification.SpringMailUserNotificator;
import com.bernardomg.security.authentication.user.notification.UserNotificator;
import com.bernardomg.security.authentication.user.test.util.model.Users;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringMailUserNotificator")
class SpringMailUserNotificatorTest {

    @Mock
    private JavaMailSender       javaMailSender;

    @Mock
    private SpringTemplateEngine templateEng;

    public SpringMailUserNotificatorTest() {
        super();
    }

    private final UserNotificator getSender() {
        return new SpringMailUserNotificator(templateEng, javaMailSender, "sender@somewhere.com",
            "http://somewhere.com");
    }

    @Test
    @DisplayName("The message content is sent to the target email")
    void testSendEmail_Content() throws Exception {
        getSender().sendUserRegisteredMessage(Users.EMAIL, Users.USERNAME, "token");

        verify(javaMailSender).send(ArgumentMatchers.any(MimeMessagePreparator.class));
    }

}
