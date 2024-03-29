
package com.bernardomg.security.authorization.access.test.config.factory;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.bernardomg.security.authorization.permission.adapter.inbound.spring.ResourceActionGrantedAuthority;
import com.bernardomg.security.authorization.permission.test.config.factory.PermissionConstants;

public final class GrantedAuthorities {

    public static ResourceActionGrantedAuthority resource() {
        return ResourceActionGrantedAuthority.builder()
            .withResource(PermissionConstants.DATA)
            .withAction(PermissionConstants.CREATE)
            .build();
    }

    public static SimpleGrantedAuthority simple() {
        return new SimpleGrantedAuthority(PermissionConstants.DATA_CREATE);
    }

    private GrantedAuthorities() {
        super();
    }

}
