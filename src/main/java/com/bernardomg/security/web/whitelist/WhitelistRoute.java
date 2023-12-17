
package com.bernardomg.security.web.whitelist;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpMethod;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder(setterPrefix = "with")
@EqualsAndHashCode
public final class WhitelistRoute {

    public static WhitelistRoute of(final String route) {
        return WhitelistRoute.builder()
            .withRoute(route)
            .withMethods(List.of())
            .build();
    }

    public static WhitelistRoute of(final String route, final HttpMethod... methods) {
        for (final HttpMethod method : methods) {
            Objects.requireNonNull(method);
        }
        return WhitelistRoute.builder()
            .withRoute(route)
            .withMethods(Arrays.asList(methods))
            .build();
    }

    private final Collection<HttpMethod> methods;

    private final String                 route;

}
