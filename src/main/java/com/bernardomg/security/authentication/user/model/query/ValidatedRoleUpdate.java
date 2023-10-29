
package com.bernardomg.security.authentication.user.model.query;

import com.bernardomg.security.authorization.role.model.request.RoleUpdate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class ValidatedRoleUpdate implements RoleUpdate {

    @NotNull
    private Long   id;

    @NotNull
    private String name;

}
