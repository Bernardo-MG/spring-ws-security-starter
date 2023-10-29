
package com.bernardomg.mvc.error.test.util.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.util.TypeInformation;
import org.springframework.http.MediaType;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PersistenceExceptionTestController.PATH)
public class PersistenceExceptionTestController {

    public static final String PATH                    = "/test/exception";

    public static final String PATH_DATA_INTEGRITY     = PATH + "/data_integrity";

    public static final String PATH_JDBC_GRAMMAR       = PATH + "/jdbc_grammar";

    public static final String PATH_PROPERTY_REFERENCE = PATH + "/property_reference";

    public PersistenceExceptionTestController() {
        super();
    }

    @GetMapping(path = "/data_integrity", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Object> dataIntegrityException() {
        throw new DataIntegrityViolationException("Data integrity error");
    }

    @GetMapping(path = "/jdbc_grammar", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Object> jdbcGrammarException() {
        throw new BadSqlGrammarException("", "", new SQLException("", "", 0));
    }

    @SuppressWarnings("unchecked")
    @GetMapping(path = "/property_reference", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Object> propertyReferenceException() {
        final TypeInformation<String> info;

        info = mock(TypeInformation.class);
        given(info.getType()).willReturn(String.class);

        throw new PropertyReferenceException("property", info, Collections.emptyList());
    }

}
