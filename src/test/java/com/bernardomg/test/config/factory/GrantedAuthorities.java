
package com.bernardomg.test.config.factory;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.bernardomg.security.permission.test.config.factory.PermissionConstants;
import com.bernardomg.security.springframework.domain.model.ResourceActionGrantedAuthority;

public final class GrantedAuthorities {

    public static ResourceActionGrantedAuthority alternativeResourceCreate() {
        return new ResourceActionGrantedAuthority(PermissionConstants.ALTERNATIVE_RESOURCE, PermissionConstants.CREATE);
    }

    public static ResourceActionGrantedAuthority resourceCreate() {
        return new ResourceActionGrantedAuthority(PermissionConstants.DATA, PermissionConstants.CREATE);
    }

    public static ResourceActionGrantedAuthority resourceRead() {
        return new ResourceActionGrantedAuthority(PermissionConstants.DATA, PermissionConstants.READ);
    }

    public static SimpleGrantedAuthority simpleCreate() {
        return new SimpleGrantedAuthority(PermissionConstants.DATA_CREATE);
    }

    private GrantedAuthorities() {
        super();
    }

}
