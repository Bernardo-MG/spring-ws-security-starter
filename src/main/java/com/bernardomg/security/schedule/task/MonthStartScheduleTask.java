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

package com.bernardomg.security.schedule.task;

import java.time.YearMonth;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import com.bernardomg.event.emitter.EventEmitter;
import com.bernardomg.security.schedule.event.MonthStartEvent;

public class MonthStartScheduleTask {

    /**
     * Logger for the class.
     */
    private static final Logger log = LoggerFactory.getLogger(MonthStartScheduleTask.class);

    private final EventEmitter  eventEmitter;

    public MonthStartScheduleTask(final EventEmitter eventEmit) {
        super();

        // TODO: move to a generic library
        eventEmitter = Objects.requireNonNull(eventEmit);
    }

    @Async
    @Scheduled(cron = "@monthly")
    public void registerMonthFees() {
        log.info("Notifying new month");
        eventEmitter.emit(new MonthStartEvent(this, YearMonth.now()));
        log.info("Notified new month");
    }

}
