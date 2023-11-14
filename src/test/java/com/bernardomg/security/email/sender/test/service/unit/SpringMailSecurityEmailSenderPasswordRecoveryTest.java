
package com.bernardomg.security.email.sender.test.service.unit;

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

import com.bernardomg.security.authentication.user.test.util.model.Users;
import com.bernardomg.security.email.sender.SecurityMessageSender;
import com.bernardomg.security.email.sender.SpringMailSecurityEmailSender;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringMailSecurityEmailSender - Password recovery")
public class SpringMailSecurityEmailSenderPasswordRecoveryTest {

    @Mock
    private JavaMailSender       javaMailSender;

    @Mock
    private SpringTemplateEngine templateEng;

    public SpringMailSecurityEmailSenderPasswordRecoveryTest() {
        super();
    }

    private final SecurityMessageSender getSender() {
        return new SpringMailSecurityEmailSender(templateEng, javaMailSender, "sender@somewhere.com",
            "http://somewhere.com", "http://somewhere.com");
    }

    @Test
    @DisplayName("The message content is sent to the target email")
    void testSendEmail_Content() throws Exception {
        getSender().sendPasswordRecoveryMessage(Users.EMAIL, Users.USERNAME, "token");

        verify(javaMailSender).send(ArgumentMatchers.any(MimeMessagePreparator.class));
    }

}
