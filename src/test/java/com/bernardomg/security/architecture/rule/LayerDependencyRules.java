/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2024-2025 the original author or authors.
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

package com.bernardomg.security.architecture.rule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.bernardomg.framework.testing.architecture.predicates.IsInServicePackage;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Dependency rules.
 */
public final class LayerDependencyRules {

    /**
     * Log4j utils are not imported.
     */
    @ArchTest
    static final ArchRule services_not_import_spring_data = noClasses().that(new IsInServicePackage())
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage("org.springframework.data..");

    private LayerDependencyRules() {
        super();
    }

}
