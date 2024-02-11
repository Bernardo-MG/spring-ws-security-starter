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

package com.bernardomg.security.authorization.permission.adapter.inbound.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bernardomg.security.authorization.permission.adapter.inbound.jpa.model.ResourceEntity;

/**
 * Resource repository based on Spring Data repositories.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public interface ResourceSpringRepository extends JpaRepository<ResourceEntity, Long> {

    /**
     * Checks if a resource exists for the received name.
     *
     * @param name
     *            resource name
     * @return {@code true} if the resource exists, {@code false} otherwise
     */
    public boolean existsByName(final String name);

    /**
     * Returns the resource for the received name.
     *
     * @param name
     *            name of the resource to search for
     * @return the resource for the received name
     */
    public Optional<ResourceEntity> findByName(final String name);

}
