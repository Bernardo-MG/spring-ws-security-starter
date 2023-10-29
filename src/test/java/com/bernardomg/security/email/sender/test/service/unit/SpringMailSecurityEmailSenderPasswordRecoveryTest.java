
package com.bernardomg.security.email.sender.test.service.unit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.bernardomg.email.EmailSender;
import com.bernardomg.security.email.sender.SecurityMessageSender;
import com.bernardomg.security.email.sender.SpringMailSecurityEmailSender;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringMailSecurityEmailSender - Password recovery")
public class SpringMailSecurityEmailSenderPasswordRecoveryTest {

    @Mock
    private EmailSender          emailSender;

    @Mock
    private JavaMailSender       javaMailSender;

    @Mock
    private SpringTemplateEngine templateEng;

    public SpringMailSecurityEmailSenderPasswordRecoveryTest() {
        super();
    }

    @BeforeEach
    public void initializeMailSender() {
        given(templateEng.process(ArgumentMatchers.anyString(), ArgumentMatchers.any(Context.class)))
            .willReturn("Content");
    }

    private final SecurityMessageSender getSender() {
        return new SpringMailSecurityEmailSender(templateEng, "http://somewhere.com", "http://somewhere.com",
            emailSender);
    }

    @Test
    @DisplayName("The message content is sent to the target email")
    void testSendEmail_Content() throws Exception {
        final ArgumentCaptor<String> captor;

        getSender().sendPasswordRecoveryMessage("email@somewhere.com", "username", "token");

        captor = ArgumentCaptor.forClass(String.class);
        verify(emailSender).sendEmail(ArgumentMatchers.any(), ArgumentMatchers.any(), captor.capture());

        Assertions.assertThat(captor.getValue())
            .isEqualTo("Content");
    }

}
