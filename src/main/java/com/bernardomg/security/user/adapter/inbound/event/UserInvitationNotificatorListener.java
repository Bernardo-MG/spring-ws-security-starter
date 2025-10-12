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

package com.bernardomg.security.user.adapter.inbound.event;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bernardomg.event.listener.EventListener;
import com.bernardomg.security.user.domain.event.UserInvitationEvent;
import com.bernardomg.security.user.usecase.service.UserNotificationService;

/**
 * Listens for login failure events, and blocks the user after a number of failures.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
public final class UserInvitationNotificatorListener implements EventListener<UserInvitationEvent> {

    /**
     * Logger for the class.
     */
    private static final Logger           log = LoggerFactory.getLogger(UserInvitationNotificatorListener.class);

    private final UserNotificationService userNotificationService;

    public UserInvitationNotificatorListener(final UserNotificationService userNotificationService) {
        super();

        this.userNotificationService = Objects.requireNonNull(userNotificationService);
    }

    @Override
    public final Class<UserInvitationEvent> getEventType() {
        return UserInvitationEvent.class;
    }

    @Override
    public final void handle(final UserInvitationEvent event) {
        log.debug("Handling invitation notification for user {}", event.getUser().username());
        userNotificationService.sendUserInvitation(event.getUser(), event.getToken());
    }

}
