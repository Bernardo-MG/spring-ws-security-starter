
package com.bernardomg.security.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan;

import jakarta.persistence.EntityManager;

@AutoConfiguration
@ConditionalOnClass(EntityManager.class)
@ComponentScan("com.bernardomg.security.adapter.inbound.jpa")
@AutoConfigurationPackage(basePackages = "com.bernardomg.security.adapter.inbound.jpa")
public class JpaSecurityAutoConfiguration {

    public JpaSecurityAutoConfiguration() {
        super();
    }

}
