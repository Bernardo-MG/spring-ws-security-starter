
package com.bernardomg.security.adapter.inbound.startup;

import java.util.Objects;

import com.bernardomg.security.usecase.initializer.loader.PermissionsLoaderService;

public final class PermissionsStartupLoader {

    private final PermissionsLoaderService service;

    public PermissionsStartupLoader(final PermissionsLoaderService service) {
        super();

        this.service = Objects.requireNonNull(service);
    }

    public final void load() {
        service.load();
    }

}
