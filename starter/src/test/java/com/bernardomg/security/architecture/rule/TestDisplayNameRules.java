package com.bernardomg.security.architecture.rule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

public class TestDisplayNameRules {

    @ArchTest
    static final ArchRule test_classes_should_have_structured_display_names = classes().that(
            new DescribedPredicate<JavaClass>("JUnit test classes") {

                @Override
                public boolean test(final JavaClass item) {
                    return !item.getModifiers()
                        .contains(JavaModifier.ABSTRACT) && item.getMethods()
                        .stream()
                        .anyMatch(method -> method.isAnnotatedWith(Test.class));
                }
            })
        .should(new ArchCondition<JavaClass>("have a display name in the form 'subject - operation'") {

            @Override
            public void check(final JavaClass item, final ConditionEvents events) {
                final String displayName;

                if (!item.isAnnotatedWith(DisplayName.class)) {
                    events.add(SimpleConditionEvent.violated(item,
                            String.format("Test class %s has no @DisplayName", item.getName())));
                    return;
                }

                displayName = item.getAnnotationOfType(DisplayName.class)
                    .value();
                if (!displayName.matches(".+ - .+")) {
                    events.add(SimpleConditionEvent.violated(item,
                            String.format("Test class %s has invalid display name '%s'", item.getName(), displayName)));
                }
            }
        });

    @ArchTest
    static final ArchRule test_methods_should_have_structured_display_names = methods().that()
        .areAnnotatedWith(Test.class)
        .should(new ArchCondition<JavaMethod>("have a display name in the form 'When ..., then ...'") {

            @Override
            public void check(final JavaMethod item, final ConditionEvents events) {
                final String displayName;

                if (!item.isAnnotatedWith(DisplayName.class)) {
                    events.add(SimpleConditionEvent.violated(item,
                            String.format("Test method %s has no @DisplayName", item.getFullName())));
                    return;
                }

                displayName = item.getAnnotationOfType(DisplayName.class)
                    .value();
                if (!displayName.matches("When .+, then .+")) {
                    events.add(SimpleConditionEvent.violated(item,
                            String.format("Test method %s has invalid display name '%s'", item.getFullName(),
                                    displayName)));
                }
            }
        });

}