
package com.bernardomg.security.springframework.test.access.usecase.validator;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import com.bernardomg.security.access.RequireResourceAccess;
import com.bernardomg.security.springframework.access.usecase.validator.RequireResourceAccessInterceptor;
import com.bernardomg.security.springframework.access.usecase.validator.ResourceAccessValidator;

@ExtendWith(MockitoExtension.class)
@DisplayName("RequireResourceAccessInterceptor")
class TestRequireResourceAccessInterceptor {

    static class TestService {

        @RequireResourceAccess(resource = "data", action = "read")
        public void annotatedMethod() {}

    }

    @InjectMocks
    private RequireResourceAccessInterceptor interceptor;

    @Mock
    private JoinPoint                        jp;

    @Mock
    private ResourceAccessValidator          validator;

    @Test
    void allowsExecution_WhenAuthorized() throws Exception {
        final Method           method;
        final ThrowingCallable exec;

        // GIVEN
        method = TestService.class.getMethod("annotatedMethod");

        // WHEN
        given(validator.isAuthorized("data", "read")).willReturn(true);

        exec = () -> interceptor.before(jp, method.getAnnotation(RequireResourceAccess.class));
        assertThatCode(exec).doesNotThrowAnyException();

        // THEN
        verify(validator).isAuthorized("data", "read");
    }

    @Test
    void deniesExecution_WhenNotAuthorized() throws Exception {
        final Method           method;
        final ThrowingCallable exec;

        // GIVEN
        method = TestService.class.getMethod("annotatedMethod");

        // WHEN
        given(validator.isAuthorized("data", "read")).willReturn(false);

        // THEN
        exec = () -> interceptor.before(jp, method.getAnnotation(RequireResourceAccess.class));
        assertThatThrownBy(exec).isInstanceOf(AccessDeniedException.class);
    }

}
