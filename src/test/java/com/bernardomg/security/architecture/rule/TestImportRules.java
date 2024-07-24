
package com.bernardomg.security.architecture.rule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaMethodCall;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

public class TestImportRules {

    @ArchTest
    static final ArchRule not_use_mockito_doThrow = noClasses().should()
        .callMethodWhere(new DescribedPredicate<JavaMethodCall>("method org.mockito.Mockito.doThrow") {

            @Override
            public boolean test(final JavaMethodCall t) {
                return t.getTargetOwner()
                    .isAssignableTo(org.mockito.Mockito.class) && "doThrow".equals(t.getName());
            }
        });

    @ArchTest
    static final ArchRule not_use_mockito_when    = noClasses().should()
        .callMethodWhere(new DescribedPredicate<JavaMethodCall>("method org.mockito.Mockito.when") {

            @Override
            public boolean test(final JavaMethodCall t) {
                return t.getTargetOwner()
                    .isAssignableTo(org.mockito.Mockito.class) && "when".equals(t.getName());
            }
        });

}
