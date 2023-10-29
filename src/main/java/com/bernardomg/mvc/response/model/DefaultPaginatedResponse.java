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

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Default implementation of the paginated response.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 * @param <T>
 *            response content type
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class DefaultPaginatedResponse<T> implements PaginatedResponse<T> {

    /**
     * Response content.
     */
    @NonNull
    private T                      content;

    /**
     * Number of elements in the page.
     */
    private Integer                elementsInPage = -1;

    /**
     * Flags this is as the first page.
     */
    private Boolean                first          = false;

    /**
     * Flags this is as the last page.
     */
    private Boolean                last           = false;

    /**
     * Number of this page.
     */
    private Integer                page           = -1;

    /**
     * Size of this page.
     */
    private Integer                size           = -1;

    private Iterable<PropertySort> sort           = new ArrayList<>();

    /**
     * Total number of elements among all the pages.
     */
    private Long                   totalElements  = -1L;

    /**
     * Total number of pages.
     */
    private Integer                totalPages     = -1;

    /**
     * Constructs a response with the specified content.
     *
     * @param cont
     *            content
     */
    public DefaultPaginatedResponse(@NonNull final T cont) {
        super();

        content = cont;
    }

}
