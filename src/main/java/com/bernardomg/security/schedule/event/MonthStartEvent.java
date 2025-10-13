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

package com.bernardomg.security.schedule.event;

import java.time.YearMonth;
import java.util.Objects;

import com.bernardomg.event.domain.AbstractEvent;

/**
 * New month has started event.
 */
public final class MonthStartEvent extends AbstractEvent {

    private static final long serialVersionUID = 7173269718677701462L;

    private final YearMonth   month;

    public MonthStartEvent(final Object source, final YearMonth date) {
        super(source);

        month = date;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final MonthStartEvent other = (MonthStartEvent) obj;
        return Objects.equals(month, other.month);
    }

    public YearMonth getMonth() {
        return month;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), month);
    }

    @Override
    public String toString() {
        return "MonthStartEvent [month=" + month + "]";
    }

}
