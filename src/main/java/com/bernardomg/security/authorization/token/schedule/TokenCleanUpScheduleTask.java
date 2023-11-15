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

package com.bernardomg.security.authorization.token.schedule;

import java.util.Objects;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import com.bernardomg.security.authorization.token.service.UserTokenService;

import lombok.extern.slf4j.Slf4j;

/**
 * Token clean up scheduled task. It delegates the actual clean up to {@link UserTokenService}.
 * <p>
 * This clean up is executed monthly.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public class TokenCleanUpScheduleTask {

    /**
     * Token clean up service.
     */
    private final UserTokenService service;

    public TokenCleanUpScheduleTask(final UserTokenService tokenCleanUpService) {
        super();

        service = Objects.requireNonNull(tokenCleanUpService);
    }

    @Async
    @Scheduled(cron = "0 0 0 1 1/1 *")
    public void cleanUpTokens() {
        log.info("Starting token cleanup task");
        service.cleanUpTokens();
        log.info("Finished token cleanup task");
    }

}
