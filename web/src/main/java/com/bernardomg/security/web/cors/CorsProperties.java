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

package com.bernardomg.security.web.cors;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * CORS configuration properties.
 */
@ConfigurationProperties(prefix = "cors")
public final record CorsProperties(String pattern, List<String> allowedHeaders, List<String> allowedMethods,
        List<String> allowedOrigins, List<String> exposedHeaders) {

    public CorsProperties(final String pattern, final List<String> allowedHeaders, final List<String> allowedMethods,
            final List<String> allowedOrigins, final List<String> exposedHeaders) {
        if (pattern == null) {
            this.pattern = "/**";
        } else {
            this.pattern = pattern;
        }
        if (allowedHeaders == null) {
            this.allowedHeaders = List.of("*");
        } else {
            this.allowedHeaders = allowedHeaders;
        }
        if (allowedMethods == null) {
            this.allowedMethods = List.of("*");
        } else {
            this.allowedMethods = allowedMethods;
        }
        if (allowedOrigins == null) {
            this.allowedOrigins = List.of("*");
        } else {
            this.allowedOrigins = allowedOrigins;
        }
        if (exposedHeaders == null) {
            this.exposedHeaders = List.of("*");
        } else {
            this.exposedHeaders = exposedHeaders;
        }
    }

}
