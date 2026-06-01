package com.bernardomg.security.openapi.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ResourcePermissionDto
 */

@JsonTypeName("ResourcePermission")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class ResourcePermissionDto {

  private String resource;

  private String action;

  public ResourcePermissionDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ResourcePermissionDto(String resource, String action) {
    this.resource = resource;
    this.action = action;
  }

  public ResourcePermissionDto resource(String resource) {
    this.resource = resource;
    return this;
  }

  /**
   * Name of the secured resource.
   * @return resource
   */
  @NotNull 
  @Schema(name = "resource", description = "Name of the secured resource.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resource")
  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  public ResourcePermissionDto action(String action) {
    this.action = action;
    return this;
  }

  /**
   * Action permitted on the resource.
   * @return action
   */
  @NotNull 
  @Schema(name = "action", description = "Action permitted on the resource.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("action")
  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResourcePermissionDto resourcePermission = (ResourcePermissionDto) o;
    return Objects.equals(this.resource, resourcePermission.resource) &&
        Objects.equals(this.action, resourcePermission.action);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resource, action);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResourcePermissionDto {\n");
    sb.append("    resource: ").append(toIndentedString(resource)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
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

