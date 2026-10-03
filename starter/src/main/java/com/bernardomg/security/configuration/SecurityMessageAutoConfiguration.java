
package com.bernardomg.security.configuration;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.AbstractResourceBasedMessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@AutoConfiguration(after = MessageSourceAutoConfiguration.class)
public class SecurityMessageAutoConfiguration {

    @Bean
    public static BeanPostProcessor securityMessageSourcePostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessBeforeInitialization(final Object bean, final String beanName) {
                if ("messageSource".equals(beanName)) {
                    if (bean instanceof final ReloadableResourceBundleMessageSource source) {
                        source.addBasenames("classpath:security-messages");
                    } else if (bean instanceof final AbstractResourceBasedMessageSource source) {
                        source.addBasenames("security-messages");
                    }
                }

                return bean;
            }
        };
    }

}
