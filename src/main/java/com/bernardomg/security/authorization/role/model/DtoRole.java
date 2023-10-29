
package com.bernardomg.security.authorization.role.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class DtoRole implements Role {

    private final Long   id;

    private final String name;

}
