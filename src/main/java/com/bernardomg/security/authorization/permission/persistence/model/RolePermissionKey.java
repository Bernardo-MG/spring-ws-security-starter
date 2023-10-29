
package com.bernardomg.security.authorization.permission.persistence.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermissionKey implements Serializable {

    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = -7233957066746780621L;

    private Long              permissionId;

    private Long              roleId;

}
