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

package com.bernardomg.mvc.response.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Order;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Paginated response wrapping a Spring page.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 * @param <T>
 *            response content type
 */
@ToString
@EqualsAndHashCode
public final class ImmutableSpringPageResponse<T> implements PaginatedResponse<Iterable<T>> {

    /**
     * Wrapped page.
     */
    @NonNull
    private final Page<T>                page;

    private final Iterable<PropertySort> sort;

    /**
     * Constructs a response wrapping the received page.
     *
     * @param pg
     *            wrapped page
     */
    public ImmutableSpringPageResponse(@NonNull final Page<T> pg) {
        super();

        sort = pg.getSort()
            .stream()
            .map(this::getPropertySort)
            .toList();

        page = pg;
    }

    @Override
    public final Iterable<T> getContent() {
        return page.getContent();
    }

    @Override
    public final Integer getElementsInPage() {
        return page.getNumberOfElements();
    }

    @Override
    public final Boolean getFirst() {
        return page.isFirst();
    }

    @Override
    public final Boolean getLast() {
        return page.isLast();
    }

    @Override
    public final Integer getPage() {
        return page.getNumber();
    }

    @Override
    public final Integer getSize() {
        return page.getSize();
    }

    @Override
    public final Iterable<PropertySort> getSort() {
        return sort;
    }

    @Override
    public final Long getTotalElements() {
        return page.getTotalElements();
    }

    @Override
    public final Integer getTotalPages() {
        return page.getTotalPages();
    }

    private final PropertySort getPropertySort(final Order order) {
        final String direction;

        if (order.isAscending()) {
            direction = "asc";
        } else {
            direction = "desc";
        }

        return ImmutablePropertySort.builder()
            .property(order.getProperty())
            .direction(direction)
            .build();
    }

}
