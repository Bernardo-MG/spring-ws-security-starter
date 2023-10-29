
package com.bernardomg.mvc.error.test.util.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.mvc.error.test.util.model.ErrorTestObject;

import jakarta.validation.Valid;

@RestController
@RequestMapping(ErrorTestController.PATH)
public class ErrorTestController {

    public static final String PATH            = "/test/error";

    public static final String PATH_METHOD_ARG = PATH + "/methodArg";

    public ErrorTestController() {
        super();
    }

    @PostMapping(path = "/methodArg", produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorTestObject exceptionMethodArg(@Valid @RequestBody final ErrorTestObject arg)
            throws MethodArgumentNotValidException {
        return arg;
    }

}
