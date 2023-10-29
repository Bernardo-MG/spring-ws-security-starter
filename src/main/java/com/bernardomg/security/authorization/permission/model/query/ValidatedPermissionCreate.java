
package com.bernardomg.security.authorization.permission.model.query;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class ValidatedPermissionCreate implements PermissionCreate {

    @NotNull
    private Long permissionId;

}
