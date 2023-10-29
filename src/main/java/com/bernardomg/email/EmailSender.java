
package com.bernardomg.email;

public interface EmailSender {

    public void sendEmail(final String recipient, final String subject, final String content);

}
