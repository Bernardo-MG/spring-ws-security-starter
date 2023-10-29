
package com.bernardomg.mvc.error.test.util.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.validation.failure.FieldFailure;
import com.bernardomg.validation.failure.exception.FieldFailureException;

@RestController
@RequestMapping(ValidationExceptionTestController.PATH)
public class ValidationExceptionTestController {

    public static final String PATH                  = "/test/error/validation";

    public static final String PATH_FIELD_VALIDATION = PATH + "/field";

    public static final String PATH_VALIDATION       = PATH + "/generic";

    public ValidationExceptionTestController() {
        super();
    }

    @GetMapping(path = "/field", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Object> exceptionFieldValidation() {
        final FieldFailure             failure;
        final Collection<FieldFailure> failures;

        failure = FieldFailure.of("Error message", "field", "code", "value");

        failures = new ArrayList<>();
        failures.add(failure);

        throw new FieldFailureException(failures);
    }

}
