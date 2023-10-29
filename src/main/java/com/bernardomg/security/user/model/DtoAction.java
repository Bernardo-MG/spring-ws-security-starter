
package com.bernardomg.security.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class DtoAction implements Action {

    private final Long   id;

    private final String name;

}
