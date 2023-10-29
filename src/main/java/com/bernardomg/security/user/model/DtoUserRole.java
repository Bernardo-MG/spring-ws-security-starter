
package com.bernardomg.security.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class DtoUserRole implements UserRole {

    private final Long roleId;

    private final Long userId;

}
