
package com.bernardomg.security.architecture.test;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.transaction.annotation.Transactional;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.ProxyRules;

@AnalyzeClasses(packages = "com.bernardomg.security", importOptions = ImportOption.DoNotIncludeTests.class)
public class ProxyArchitectureRulesTest {

    @ArchTest
    static final ArchRule no_direct_calls_to_cacheable_method     = ProxyRules
        .no_classes_should_directly_call_other_methods_declared_in_the_same_class_that_are_annotated_with(
            Cacheable.class);

    @ArchTest
    static final ArchRule no_direct_calls_to_caching_method       = ProxyRules
        .no_classes_should_directly_call_other_methods_declared_in_the_same_class_that_are_annotated_with(
            Caching.class);

    @ArchTest
    static final ArchRule no_direct_calls_to_transactional_method = ProxyRules
        .no_classes_should_directly_call_other_methods_declared_in_the_same_class_that_are_annotated_with(
            Transactional.class);

}
