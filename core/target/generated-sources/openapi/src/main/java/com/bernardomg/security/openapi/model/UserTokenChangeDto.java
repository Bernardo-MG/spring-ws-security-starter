package com.bernardomg.security.openapi.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.Instant;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UserTokenChangeDto
 */

@JsonTypeName("UserTokenChange")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class UserTokenChangeDto {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable Instant expirationDate;

  private @Nullable Boolean revoked;

  public UserTokenChangeDto expirationDate(@Nullable Instant expirationDate) {
    this.expirationDate = expirationDate;
    return this;
  }

  /**
   * Get expirationDate
   * @return expirationDate
   */
  @Valid 
  @Schema(name = "expirationDate", example = "2025-08-01T00:00Z", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expirationDate")
  public @Nullable Instant getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(@Nullable Instant expirationDate) {
    this.expirationDate = expirationDate;
  }

  public UserTokenChangeDto revoked(@Nullable Boolean revoked) {
    this.revoked = revoked;
    return this;
  }

  /**
   * Get revoked
   * @return revoked
   */
  
  @Schema(name = "revoked", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("revoked")
  public @Nullable Boolean getRevoked() {
    return revoked;
  }

  public void setRevoked(@Nullable Boolean revoked) {
    this.revoked = revoked;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserTokenChangeDto userTokenChange = (UserTokenChangeDto) o;
    return Objects.equals(this.expirationDate, userTokenChange.expirationDate) &&
        Objects.equals(this.revoked, userTokenChange.revoked);
  }

  @Override
  public int hashCode() {
    return Objects.hash(expirationDate, revoked);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserTokenChangeDto {\n");
    sb.append("    expirationDate: ").append(toIndentedString(expirationDate)).append("\n");
    sb.append("    revoked: ").append(toIndentedString(revoked)).append("\n");
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

