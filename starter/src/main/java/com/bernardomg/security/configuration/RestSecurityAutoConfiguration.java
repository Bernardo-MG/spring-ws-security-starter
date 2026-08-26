
package com.bernardomg.security.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ConditionalOnWebApplication(type = Type.SERVLET)
@ComponentScan("com.bernardomg.security.adapter.outbound.rest")
public class RestSecurityAutoConfiguration {

}
