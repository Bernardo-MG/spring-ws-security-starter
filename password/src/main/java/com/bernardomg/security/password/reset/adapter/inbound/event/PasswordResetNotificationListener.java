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

package com.bernardomg.security.password.reset.adapter.inbound.event;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bernardomg.event.listener.EventListener;
import com.bernardomg.security.password.reset.domain.event.PasswordResetEvent;
import com.bernardomg.security.password.reset.usecase.service.PasswordNotificationService;

/**
 * Listens for login failure events, and blocks the user after a number of failures.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
public final class PasswordResetNotificationListener implements EventListener<PasswordResetEvent> {

    /**
     * Logger for the class.
     */
    private static final Logger               log = LoggerFactory.getLogger(PasswordResetNotificationListener.class);

    private final PasswordNotificationService passwordNotificationService;

    public PasswordResetNotificationListener(final PasswordNotificationService passwordNotificationService) {
        super();

        this.passwordNotificationService = Objects.requireNonNull(passwordNotificationService);
    }

    @Override
    public final Class<PasswordResetEvent> getEventType() {
        return PasswordResetEvent.class;
    }

    @Override
    public final void handle(final PasswordResetEvent event) {
        log.debug("Handling password reset notification for user {}", event.getUser()
            .username());
        passwordNotificationService.sendPasswordRecoveryMessage(event.getUser(), event.getToken());
    }

}
