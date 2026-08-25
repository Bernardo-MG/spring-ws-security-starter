
package com.bernardomg.security.test.autoconfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import com.bernardomg.security.configuration.JwtProperties;
import com.bernardomg.security.test.config.factory.JwtConstants;

@DisplayName("JwtProperties")
final class JwtPropertiesTest {

    @Configuration(proxyBeanMethods = false)
    @EnableConfigurationProperties(JwtProperties.class)
    static class JwtPropertiesConfiguration {}

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(JwtPropertiesConfiguration.class))
        .withPropertyValues("security.jwt.secret=" + JwtConstants.SECRET);

    @Test
    @DisplayName("When properties are received, they are binded")
    void testProperties_BindsJwtProperties() {
        contextRunner.withPropertyValues("security.jwt.validity=30m")
            .run(context -> {
                assertThat(context).hasNotFailed();

                final JwtProperties properties = context.getBean(JwtProperties.class);

                assertSoftly(softly -> {
                    softly.assertThat(properties.secret())
                        .as("JWT secret")
                        .isEqualTo(JwtConstants.SECRET);

                    softly.assertThat(properties.validity())
                        .as("JWT validity")
                        .isEqualTo(Duration.ofMinutes(30));
                });
            });
    }

    @Test
    @DisplayName("When validity is missing, the default value is used")
    void testProperties_UsesDefaultValidity() {
        contextRunner.run(context -> {
            assertThat(context).hasNotFailed();

            final JwtProperties properties = context.getBean(JwtProperties.class);

            assertSoftly(softly -> {
                softly.assertThat(properties.secret())
                    .as("JWT secret")
                    .isEqualTo(JwtConstants.SECRET);

                softly.assertThat(properties.validity())
                    .as("default JWT validity")
                    .isEqualTo(Duration.ofHours(1));
            });
        });
    }

}
