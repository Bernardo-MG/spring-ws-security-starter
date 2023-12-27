
package com.bernardomg.security.authorization.access.test.config.factory;

import com.bernardomg.security.authorization.springframework.ResourceActionGrantedAuthority;

public final class ResourceActionGrantedAuthorities {

    public static ResourceActionGrantedAuthority valid() {
        return ResourceActionGrantedAuthority.builder()
            .withResource("resource")
            .withAction("action")
            .build();
    }

    private ResourceActionGrantedAuthorities() {
        super();
    }

}
