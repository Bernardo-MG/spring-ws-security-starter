package com.bernardomg.security.openapi.model;

import java.net.URI;
import java.util.Objects;
import com.bernardomg.security.openapi.model.ResourcePermissionDto;
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
 * RoleDto
 */

@JsonTypeName("Role")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class RoleDto {

  private String name;

  @Valid
  private List<@Valid ResourcePermissionDto> permissions = new ArrayList<>();

  public RoleDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RoleDto(String name, List<@Valid ResourcePermissionDto> permissions) {
    this.name = name;
    this.permissions = permissions;
  }

  public RoleDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Unique name.
   * @return name
   */
  @NotNull 
  @Schema(name = "name", description = "Unique name.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public RoleDto permissions(List<@Valid ResourcePermissionDto> permissions) {
    this.permissions = permissions;
    return this;
  }

  public RoleDto addPermissionsItem(ResourcePermissionDto permissionsItem) {
    if (this.permissions == null) {
      this.permissions = new ArrayList<>();
    }
    this.permissions.add(permissionsItem);
    return this;
  }

  /**
   * List of resource permissions.
   * @return permissions
   */
  @NotNull @Valid 
  @Schema(name = "permissions", description = "List of resource permissions.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("permissions")
  public List<@Valid ResourcePermissionDto> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<@Valid ResourcePermissionDto> permissions) {
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
    RoleDto role = (RoleDto) o;
    return Objects.equals(this.name, role.name) &&
        Objects.equals(this.permissions, role.permissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, permissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RoleDto {\n");
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

