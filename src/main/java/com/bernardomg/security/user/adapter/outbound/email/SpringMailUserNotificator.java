/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023-2025 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bernardomg.security.user.adapter.outbound.email;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.bernardomg.security.user.usecase.notificator.UserNotificator;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * User notificator based on Spring Mail. The message bodies are composed with the help of Thymeleaf.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public final class SpringMailUserNotificator implements UserNotificator {

    /**
     * Logger for the class.
     */
    private static final Logger        log                   = LoggerFactory.getLogger(SpringMailUserNotificator.class);

    /**
     * Email for the from field.
     */
    private final String               fromEmail;

    /**
     * Mail sender. This will do the actual sending.
     */
    private final JavaMailSender       mailSender;

    /**
     * Template engine to generate the email content.
     */
    private final SpringTemplateEngine templateEngine;

    /**
     * Subject when the user is registered.
     */
    private final String               userRegisteredSubject = "User registered";

    /**
     * URL for the registered user email, to activate the user.
     */
    private final String               userRegisteredUrl;

    public SpringMailUserNotificator(final SpringTemplateEngine templateEng, final JavaMailSender mailSendr,
            final String frmEmail, final String userRegUrl) {
        super();

        templateEngine = Objects.requireNonNull(templateEng);
        mailSender = Objects.requireNonNull(mailSendr);
        fromEmail = Objects.requireNonNull(frmEmail);
        userRegisteredUrl = Objects.requireNonNull(userRegUrl);
    }

    @Override
    public final void sendUserRegisteredMessage(final String email, final String username, final String token) {
        final String recoveryUrl;
        final String userRegisteredEmailText;

        log.debug("Sending user registered email to {} for {}", email, username);

        recoveryUrl = generateUrl(userRegisteredUrl, token);
        userRegisteredEmailText = generateEmailContent("mail/user-registered", recoveryUrl, username);

        // TODO: Send template name and parameters
        sendEmail(email, userRegisteredSubject, userRegisteredEmailText);

        log.debug("Sent user registered email to {} for {}", email, username);
    }

    private final String generateEmailContent(final String templateName, final String url, final String username) {
        final Context context;

        context = new Context();
        context.setVariable("url", url);
        context.setVariable("username", username);
        return templateEngine.process(templateName, context);
    }

    private final String generateUrl(final String baseUrl, final String token) {
        final String url;

        if (baseUrl.endsWith("/")) {
            url = baseUrl + token;
        } else {
            url = baseUrl + "/" + token;
        }

        return url;
    }

    private final void prepareMessage(final MimeMessage mimeMessage, final String recipient, final String subject,
            final String content) throws MessagingException {
        final MimeMessageHelper messageHelper;

        messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(fromEmail);
        messageHelper.setTo(recipient);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true); // 'true' indicates HTML content
    }

    private final void sendEmail(final String recipient, final String subject, final String content) {
        final MimeMessagePreparator messagePreparator;

        messagePreparator = mimeMessage -> prepareMessage(mimeMessage, recipient, subject, content);

        try {
            mailSender.send(messagePreparator);
        } catch (final Exception e) {
            log.error("Error sending email", e);
        }
    }

}
