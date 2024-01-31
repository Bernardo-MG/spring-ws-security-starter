
package com.bernardomg.security.authentication.password.reset.adapter.outbound.rest.model;

import com.bernardomg.validation.constraint.StrongPassword;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data required for changing a password.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public final class PasswordResetChange {

    /**
     * The new password.
     */
    @NotEmpty
    @StrongPassword
    private String password;

}
