
package com.bernardomg.security.authorization.role.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class RoleQueryRequest implements RoleQuery {

    private String name;

}
