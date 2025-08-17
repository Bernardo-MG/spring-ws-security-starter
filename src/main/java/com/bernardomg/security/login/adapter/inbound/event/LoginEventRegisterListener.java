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

package com.bernardomg.security.login.adapter.inbound.event;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import com.bernardomg.event.listener.EventListener;
import com.bernardomg.security.event.LogInEvent;
import com.bernardomg.security.login.adapter.outbound.cache.Logins;
import com.bernardomg.security.login.usecase.service.LoginRegisterService;

/**
 * Listens for login events and registers them.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
public final class LoginEventRegisterListener implements EventListener<LogInEvent> {

    /**
     * Logger for the class.
     */
    private static final Logger        log = LoggerFactory.getLogger(LoginEventRegisterListener.class);

    private final LoginRegisterService loginRegisterService;

    public LoginEventRegisterListener(final LoginRegisterService loginRegisterServ) {
        super();

        loginRegisterService = Objects.requireNonNull(loginRegisterServ);
    }

    @Override
    public final Class<LogInEvent> getEventType() {
        return LogInEvent.class;
    }

    @Override
    @Caching(evict = { @CacheEvict(cacheNames = Logins.LOGIN_REGISTERS, allEntries = true) })
    public final void handle(final LogInEvent event) {
        log.debug("Handling login event register for {}", event.getUsername());
        loginRegisterService.register(event.getUsername(), event.isLoggedIn());
    }

}
