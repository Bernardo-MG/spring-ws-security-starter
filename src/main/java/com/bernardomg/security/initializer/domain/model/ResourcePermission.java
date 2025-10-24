
package com.bernardomg.security.initializer.domain.model;

import java.util.Collection;

public record ResourcePermission(String resource, Collection<String> actions) {

}
