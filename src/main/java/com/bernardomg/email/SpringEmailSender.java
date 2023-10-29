
package com.bernardomg.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class SpringEmailSender implements EmailSender {

    private final String         fromEmail;

    private final JavaMailSender mailSender;

    public SpringEmailSender(final String from, final JavaMailSender mailSender) {
        super();

        fromEmail = from;
        this.mailSender = mailSender;
    }

    @Async
    @Override
    public void sendEmail(final String recipient, final String subject, final String content) {
        final MimeMessagePreparator messagePreparator;

        messagePreparator = mimeMessage -> prepareMessage(mimeMessage, recipient, subject, content);

        try {
            mailSender.send(messagePreparator);
        } catch (final Exception e) {
            log.error("Error sending email", e);
        }
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

}
