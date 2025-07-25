
package com.bernardomg.security.web.cors;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

public final class CorsConfigurationPropertiesSource implements CorsConfigurationSource {

    private final UrlBasedCorsConfigurationSource wrapped;

    public CorsConfigurationPropertiesSource(final CorsProperties corsProperties) {
        super();
        final CorsConfiguration configuration;
        // FIXME: remove dependency to properties object

        configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsProperties.allowedOrigins());
        configuration.setAllowedMethods(corsProperties.allowedMethods());
        configuration.setAllowedHeaders(corsProperties.allowedHeaders());
        configuration.setExposedHeaders(corsProperties.exposedHeaders());

        wrapped = new UrlBasedCorsConfigurationSource();
        wrapped.registerCorsConfiguration("/**", configuration);
    }

    @Override
    public CorsConfiguration getCorsConfiguration(final HttpServletRequest request) {
        return wrapped.getCorsConfiguration(request);
    }

}
