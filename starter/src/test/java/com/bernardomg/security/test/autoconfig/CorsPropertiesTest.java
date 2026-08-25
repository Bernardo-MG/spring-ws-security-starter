
package com.bernardomg.security.test.autoconfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import com.bernardomg.security.configuration.CorsProperties;

@DisplayName("CorsProperties")
final class CorsPropertiesTest {

    @Configuration(proxyBeanMethods = false)
    @EnableConfigurationProperties(CorsProperties.class)
    static class CorsPropertiesConfiguration {}

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withUserConfiguration(CorsPropertiesConfiguration.class);

    @Test
    @DisplayName("When no properties are received, defaults are applied")
    void testProperties_AppliesDefaults() {
        contextRunner.run(context -> {
            final CorsProperties properties;

            assertThat(context).hasNotFailed();

            properties = context.getBean(CorsProperties.class);

            assertSoftly(softly -> {
                softly.assertThat(properties.pattern())
                    .as("pattern")
                    .isEqualTo("/**");

                softly.assertThat(properties.allowedHeaders())
                    .as("allowed headers")
                    .containsExactly("Authorization", "Content-Type");

                softly.assertThat(properties.allowedMethods())
                    .as("allowed methods")
                    .containsExactly("GET");

                softly.assertThat(properties.allowedOrigins())
                    .as("allowed origins")
                    .isEmpty();

                softly.assertThat(properties.exposedHeaders())
                    .as("exposed headers")
                    .isEmpty();
            });
        });
    }

    @Test
    @DisplayName("When properties are received, they are binded")
    void testProperties_BindsConfiguredValues() {
        contextRunner
            .withPropertyValues("security.cors.pattern=/api/**", "security.cors.allowed-origins=https://example.com",
                "security.cors.allowed-methods=GET,POST",
                "security.cors.allowed-headers=Authorization,Content-Type,X-Test",
                "security.cors.exposed-headers=X-Request-Id")
            .run(context -> {
                final CorsProperties properties;

                assertThat(context).hasNotFailed();

                properties = context.getBean(CorsProperties.class);

                assertSoftly(softly -> {
                    softly.assertThat(properties.pattern())
                        .as("pattern")
                        .isEqualTo("/api/**");

                    softly.assertThat(properties.allowedOrigins())
                        .as("allowed origins")
                        .containsExactly("https://example.com");

                    softly.assertThat(properties.allowedMethods())
                        .as("allowed methods")
                        .containsExactly("GET", "POST");

                    softly.assertThat(properties.allowedHeaders())
                        .as("allowed headers")
                        .containsExactly("Authorization", "Content-Type", "X-Test");

                    softly.assertThat(properties.exposedHeaders())
                        .as("exposed headers")
                        .containsExactly("X-Request-Id");
                });
            });
    }

}
