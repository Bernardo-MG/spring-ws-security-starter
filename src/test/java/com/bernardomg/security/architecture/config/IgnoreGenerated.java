
package com.bernardomg.security.architecture.config;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;

public final class IgnoreGenerated implements ImportOption {

    @Override
    public final boolean includes(final Location location) {
        return (!location.contains("/com/bernardomg/security/openapi/") && !location.contains(".com.bernardomg.security.openapi."));
    }

}
