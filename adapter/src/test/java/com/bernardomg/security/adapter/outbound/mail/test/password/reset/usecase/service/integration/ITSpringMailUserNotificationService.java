
package com.bernardomg.security.adapter.outbound.mail.test.password.reset.usecase.service.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

import java.util.Locale;
import java.util.Properties;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.bernardomg.security.adapter.outbound.mail.user.usecase.service.SpringMailUserNotificationService;
import com.bernardomg.security.domain.user.model.User;
import com.bernardomg.security.usecase.user.service.UserNotificationService;

import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringMailUserNotificationService - production template and messages")
class ITSpringMailUserNotificationService {

    @Mock
    private JavaMailSender          javaMailSender;

    private LocaleContext           previousLocaleContext;

    private UserNotificationService userNotificationService;

    private String htmlBody(final Part part) throws Exception {
        final Multipart multipart;

        if (part.isMimeType("text/html")) {
            return (String) part.getContent();
        }

        if (part.isMimeType("multipart/*")) {
            multipart = (Multipart) part.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                final String html;

                html = htmlBody(multipart.getBodyPart(i));
                if (!html.isEmpty()) {
                    return html;
                }
            }
        }

        return "";
    }

    @BeforeEach
    void initializeService() {
        final ClassLoaderTemplateResolver resolver;
        final SpringTemplateEngine        templateEngine;
        final ResourceBundleMessageSource messageSource;

        previousLocaleContext = LocaleContextHolder.getLocaleContext();

        resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);

        messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("security-messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setUseCodeAsDefaultMessage(false);

        templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        templateEngine.setTemplateEngineMessageSource(messageSource);

        userNotificationService = new SpringMailUserNotificationService(templateEngine, javaMailSender,
            "sender@example.com", "https://example.com/activate", "Example App", messageSource);
    }

    @AfterEach
    void restoreLocale() {
        LocaleContextHolder.setLocaleContext(previousLocaleContext);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = { "en-GB", "es-ES" })
    @DisplayName("When sending a user invitation, then the production template and messages are rendered")
    void testSendUserInvitation_TemplateAndMessagesRendered(final String languageTag) throws Exception {
        final User                    user;
        final String                  token;
        final MimeMessage             message;
        final MimeMessagePreparator[] preparators;
        final String                  html;
        final SoftAssertions          softly;

        // GIVEN
        LocaleContextHolder.setLocale(Locale.forLanguageTag(languageTag));

        user = User.newUser("alice", "alice@example.com", "Alice");
        token = "integration-test-token";
        message = new MimeMessage(Session.getInstance(new Properties()));
        preparators = new MimeMessagePreparator[1];

        doAnswer(invocation -> {
            preparators[0] = invocation.getArgument(0);
            return null;
        }).when(javaMailSender)
            .send(any(MimeMessagePreparator.class));

        // WHEN
        userNotificationService.sendUserInvitation(user, token);

        if (preparators[0] != null) {
            preparators[0].prepare(message);
            message.saveChanges();
        }

        // THEN
        softly = new SoftAssertions();
        softly.assertThat(preparators[0])
            .as("Email preparator")
            .isNotNull();

        if (preparators[0] != null) {
            html = htmlBody(message);

            softly.assertThat(message.getFrom())
                .as("Sender")
                .extracting(Object::toString)
                .containsExactly("sender@example.com");
            softly.assertThat(message.getAllRecipients())
                .as("Recipients")
                .extracting(Object::toString)
                .containsExactly(user.email());
            softly.assertThat(message.getSubject())
                .as("Resolved subject")
                .isNotBlank()
                .contains("Example App")
                .doesNotContain("email.welcome.title", "{0}", "??");
            softly.assertThat(html)
                .as("Rendered HTML with resolved messages")
                .isNotBlank()
                .contains("https://example.com/activate/" + token)
                .doesNotContain("${url}", "??");
        }

        softly.assertAll();
    }

}
