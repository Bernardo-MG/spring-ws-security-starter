/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2023 the original author or authors.
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

package com.bernardomg.security.authentication.password.notification;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Email sender for security operations which integrates with Spring Mail.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class SpringMailPasswordNotificator implements PasswordNotificator {

    private final String               fromEmail;

    private final JavaMailSender       mailSender;

    private final String               passwordRecoverySubject = "Password recovery";

    private final String               passwordRecoveryUrl;

    private final SpringTemplateEngine templateEngine;

    public SpringMailPasswordNotificator(final SpringTemplateEngine templateEng, final JavaMailSender mailSendr,
            final String frmEmail, final String passRecoveryUrl) {
        super();

        templateEngine = templateEng;
        mailSender = mailSendr;
        fromEmail = frmEmail;
        passwordRecoveryUrl = passRecoveryUrl;
    }

    @Override
    public final void sendPasswordRecoveryMessage(final String email, final String username, final String token) {
        final String recoveryUrl;
        final String passwordRecoveryEmailText;

        log.debug("Sending password recovery email to {} for {}", email, username);

        recoveryUrl = generateUrl(passwordRecoveryUrl, token);
        passwordRecoveryEmailText = generateEmailContent("mail/password-recovery", recoveryUrl, username);

        sendEmail(email, passwordRecoverySubject, passwordRecoveryEmailText);

        log.debug("Sent password recovery email to {} for {}", email, username);
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
