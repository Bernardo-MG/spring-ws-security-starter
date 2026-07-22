
package com.bernardomg.security.springframework.access.interceptor;

import org.springframework.security.core.Authentication;

public interface ResourcePermissionEvaluator {

    boolean isAuthorized(final Authentication authentication, final String resource, final String action);

}
