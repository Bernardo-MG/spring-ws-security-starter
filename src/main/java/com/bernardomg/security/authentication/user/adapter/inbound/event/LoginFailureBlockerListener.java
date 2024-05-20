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

package com.bernardomg.security.authentication.user.adapter.inbound.event;

import org.springframework.context.ApplicationListener;

import com.bernardomg.security.authentication.user.usecase.service.UserAccessService;
import com.bernardomg.security.login.domain.event.LogInEvent;

/**
 * Listens for login failure events, and blocks the user after a number of failures.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
public final class LoginFailureBlockerListener implements ApplicationListener<LogInEvent> {

    private final UserAccessService userAccessService;

    public LoginFailureBlockerListener(final UserAccessService userAccessServ) {
        super();

        userAccessService = userAccessServ;
    }

    @Override
    public final void onApplicationEvent(final LogInEvent event) {
        if (event.isLoggedIn()) {
            userAccessService.clear(event.getUsername());
        } else {
            userAccessService.checkForLocking(event.getUsername());
        }
    }

}
