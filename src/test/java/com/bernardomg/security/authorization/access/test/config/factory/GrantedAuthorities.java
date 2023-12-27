
package com.bernardomg.security.authorization.access.test.config.factory;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.bernardomg.security.authorization.permission.test.config.factory.PermissionConstants;
import com.bernardomg.security.authorization.springframework.ResourceActionGrantedAuthority;

public final class GrantedAuthorities {

    public static ResourceActionGrantedAuthority resource() {
        return ResourceActionGrantedAuthority.builder()
            .withResource(PermissionConstants.RESOURCE)
            .withAction(PermissionConstants.ACTION)
            .build();
    }

    public static SimpleGrantedAuthority simple() {
        return new SimpleGrantedAuthority(PermissionConstants.PERMISSION);
    }

    private GrantedAuthorities() {
        super();
    }

}
