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

package com.bernardomg.security.audit;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Listens for audit events and logs them.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Component
@Slf4j
public class AuditEventLogger {

    public AuditEventLogger() {
        super();
    }

    @EventListener
    public void auditEventHappened(final AuditApplicationEvent auditApplicationEvent) {
        final AuditEvent auditEvent;
        final Object     details;
        final Object     message;

        auditEvent = auditApplicationEvent.getAuditEvent();

        log.debug("Audit event {} for {}", auditEvent.getType(), auditEvent.getPrincipal());

        message = auditEvent.getData()
            .get("message");
        if (message != null) {
            log.debug("{}", message);
        }

        details = auditEvent.getData()
            .get("details");
        if (details instanceof final WebAuthenticationDetails webDetails) {
            log.debug("Remote IP address: {}", webDetails.getRemoteAddress());
            log.debug("Session Id: {}", webDetails.getSessionId());
        }
    }

}
