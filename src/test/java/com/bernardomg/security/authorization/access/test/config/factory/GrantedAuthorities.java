
package com.bernardomg.security.authorization.access.test.config.factory;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.bernardomg.security.authorization.permission.test.config.factory.PermissionConstants;
import com.bernardomg.security.spring.usecase.ResourceActionGrantedAuthority;

public final class GrantedAuthorities {

    public static ResourceActionGrantedAuthority alternativeResourceCreate() {
        return ResourceActionGrantedAuthority.builder()
            .withResource(PermissionConstants.ALTERNATIVE_RESOURCE)
            .withAction(PermissionConstants.CREATE)
            .build();
    }

    public static ResourceActionGrantedAuthority resourceCreate() {
        return ResourceActionGrantedAuthority.builder()
            .withResource(PermissionConstants.DATA)
            .withAction(PermissionConstants.CREATE)
            .build();
    }

    public static ResourceActionGrantedAuthority resourceRead() {
        return ResourceActionGrantedAuthority.builder()
            .withResource(PermissionConstants.DATA)
            .withAction(PermissionConstants.READ)
            .build();
    }

    public static SimpleGrantedAuthority simpleCreate() {
        return new SimpleGrantedAuthority(PermissionConstants.DATA_CREATE);
    }

    private GrantedAuthorities() {
        super();
    }

}
