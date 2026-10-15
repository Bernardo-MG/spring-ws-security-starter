package com.bernardomg.security.adapter.inbound.jpa.repository.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.bernardomg.security.adapter.test.config.TestConfiguration;

/**
 * Application runnable class. This allows Spring Boot to run the application.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@SpringBootApplication
@Import({ TestConfiguration.class })
public class TestApplication {

    /**
     * Runnable main method.
     *
     * @param args
     *            execution parameters
     */
    public static void main(final String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    /**
     * Default constructor.
     */
    public TestApplication() {
        super();
    }

}
