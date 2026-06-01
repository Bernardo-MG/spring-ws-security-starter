package com.bernardomg.security.openapi.model;

import java.net.URI;
import java.util.Objects;
import com.bernardomg.security.openapi.model.RoleChangePermissionDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RoleChangeDto
 */

@JsonTypeName("RoleChange")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class RoleChangeDto {

  @Valid
  private List<@Valid RoleChangePermissionDto> permissions = new ArrayList<>();

  public RoleChangeDto permissions(List<@Valid RoleChangePermissionDto> permissions) {
    this.permissions = permissions;
    return this;
  }

  public RoleChangeDto addPermissionsItem(RoleChangePermissionDto permissionsItem) {
    if (this.permissions == null) {
      this.permissions = new ArrayList<>();
    }
    this.permissions.add(permissionsItem);
    return this;
  }

  /**
   * Updated permissions for the role.
   * @return permissions
   */
  @Valid 
  @Schema(name = "permissions", description = "Updated permissions for the role.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("permissions")
  public List<@Valid RoleChangePermissionDto> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<@Valid RoleChangePermissionDto> permissions) {
    this.permissions = permissions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RoleChangeDto roleChange = (RoleChangeDto) o;
    return Objects.equals(this.permissions, roleChange.permissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(permissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RoleChangeDto {\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

