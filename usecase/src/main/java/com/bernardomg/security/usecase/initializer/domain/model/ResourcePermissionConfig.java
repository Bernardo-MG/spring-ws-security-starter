
package com.bernardomg.security.usecase.initializer.domain.model;

import java.util.Collection;

public record ResourcePermissionConfig(String resource, Collection<String> actions) {

}
