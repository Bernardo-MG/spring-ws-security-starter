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

package com.bernardomg.security.access.interceptor;

import java.util.Objects;
import java.util.function.Supplier;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bernardomg.security.access.annotation.RequireResourceAuthorization;

/**
 * Intercepts calls to any method marked by {@code AuthorizedResource} and applies resource-based authentication. This
 * is done with aspects.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Aspect
public final class RequireResourceAuthorizationInterceptor {

    /**
     * Logger for the class.
     */
    private static final Logger              log = LoggerFactory
        .getLogger(RequireResourceAuthorizationInterceptor.class);

    /**
     * Authorization validator. Makes sure the user in session has the required authorities.
     */
    private final ResourceAccessValidator    accessValidator;

    private final Supplier<RuntimeException> exceptionSupplier;

    public RequireResourceAuthorizationInterceptor(final ResourceAccessValidator validator,
            final Supplier<RuntimeException> exceptionSup) {
        super();

        accessValidator = Objects.requireNonNull(validator);
        exceptionSupplier = Objects.requireNonNull(exceptionSup);
    }

    @Before("@annotation(RequireResourceAccess)")
    public final void before(final JoinPoint call, final RequireResourceAuthorization annotation) {
        final boolean authorized;

        authorized = accessValidator.isAuthorized(annotation.resource(), annotation.action());

        if (!authorized) {
            log.error("User is not authorized with action {} for resource {}", annotation.action(),
                annotation.resource());
            throw exceptionSupplier.get();
        }
    }

}
