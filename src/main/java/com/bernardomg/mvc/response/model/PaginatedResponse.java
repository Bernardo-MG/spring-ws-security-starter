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

/**
 * Paginated response to the frontend.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 * @param <T>
 *            response content type
 */
public interface PaginatedResponse<T> extends Response<T> {

    /**
     * Number of elements in the page.
     *
     * @return number of elements
     */
    public Integer getElementsInPage();

    /**
     * Flags this is as the first page.
     *
     * @return {@code true} if this is the first page, {@code false} otherwise
     */
    public Boolean getFirst();

    /**
     * Flags this is as the last page.
     *
     * @return {@code true} if this is the last page, {@code false} otherwise
     */
    public Boolean getLast();

    /**
     * Number of this page.
     *
     * @return the number of this page
     */
    public Integer getPage();

    /**
     * Size of this page.
     *
     * @return the size of this page.
     */
    public Integer getSize();

    /**
     * Properties used for sorting.
     *
     * @return properties used for sorting
     */
    public Iterable<PropertySort> getSort();

    /**
     * Total number of elements among all the pages.
     *
     * @return the total number of elements
     */
    public Long getTotalElements();

    /**
     * Total number of pages.
     *
     * @return the total number of pages
     */
    public Integer getTotalPages();

}
