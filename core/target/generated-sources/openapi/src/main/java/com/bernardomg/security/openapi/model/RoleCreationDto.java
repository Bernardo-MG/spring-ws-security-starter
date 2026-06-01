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
 * RoleCreationDto
 */

@JsonTypeName("RoleCreation")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class RoleCreationDto {

  private String name;

  @Valid
  private List<@Valid RoleChangePermissionDto> permissions = new ArrayList<>();

  public RoleCreationDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RoleCreationDto(String name, List<@Valid RoleChangePermissionDto> permissions) {
    this.name = name;
    this.permissions = permissions;
  }

  public RoleCreationDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Name for the new role.
   * @return name
   */
  @NotNull 
  @Schema(name = "name", description = "Name for the new role.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public RoleCreationDto permissions(List<@Valid RoleChangePermissionDto> permissions) {
    this.permissions = permissions;
    return this;
  }

  public RoleCreationDto addPermissionsItem(RoleChangePermissionDto permissionsItem) {
    if (this.permissions == null) {
      this.permissions = new ArrayList<>();
    }
    this.permissions.add(permissionsItem);
    return this;
  }

  /**
   * Get permissions
   * @return permissions
   */
  @NotNull @Valid 
  @Schema(name = "permissions", requiredMode = Schema.RequiredMode.REQUIRED)
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
    RoleCreationDto roleCreation = (RoleCreationDto) o;
    return Objects.equals(this.name, roleCreation.name) &&
        Objects.equals(this.permissions, roleCreation.permissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, permissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RoleCreationDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

