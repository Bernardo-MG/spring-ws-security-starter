/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2022-2023 the original author or authors.
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

package com.bernardomg.security.web.whitelist;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Whitelist filter wrapper. Will ignore any path on the whitelist, otherwise it applies the wrapped filter.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class WhitelistFilterWrapper extends OncePerRequestFilter {

    private final Filter                     filter;

    /**
     * Whitelisted routes, these should be ignored.
     */
    private final Collection<WhitelistRoute> whitelist;

    /**
     * Constructs a whitelist filter filter wrapper.
     *
     * @param fltr
     *            wrapped filter
     * @param whitel
     *            whitelisted routes
     */
    public WhitelistFilterWrapper(final Filter fltr, final Collection<WhitelistRoute> whitel) {
        super();

        filter = Objects.requireNonNull(fltr);
        whitelist = Objects.requireNonNull(whitel);
    }

    @Override
    protected final void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) throws ServletException, IOException {
        filter.doFilter(request, response, chain);
    }

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException {
        final HttpMethod method;

        method = HttpMethod.valueOf(request.getMethod());

        return whitelist.stream()
            .anyMatch(w -> w.getRoute()
                .equals(request.getRequestURI())
                    && w.getMethods()
                        .contains(method));
    }

}
