
package com.bernardomg.mvc.error.test.util.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ExceptionTestController.PATH)
public class ExceptionTestController {

    public static final String PATH         = "/test/exception";

    public static final String PATH_RUNTIME = PATH + "/runtime";

    public ExceptionTestController() {
        super();
    }

    @GetMapping(path = "/runtime", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Object> runtimeException() {
        throw new RuntimeException("Error message");
    }

}
