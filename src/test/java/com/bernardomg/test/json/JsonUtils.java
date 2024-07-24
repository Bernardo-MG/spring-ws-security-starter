package com.bernardomg.test.json;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils {

    public static final String toJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private JsonUtils() {
        super();
    }

}
