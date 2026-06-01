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
 * Token issued for a user, including its lifecycle states.
 */

@Schema(name = "UserToken", description = "Token issued for a user, including its lifecycle states.")
@JsonTypeName("UserToken")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class UserTokenDto {

  private String username;

  private @Nullable String name;

  private String scope;

  private String token;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant creationDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant expirationDate;

  private Boolean consumed;

  private Boolean revoked;

  public UserTokenDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UserTokenDto(String username, String scope, String token, Instant creationDate, Instant expirationDate, Boolean consumed, Boolean revoked) {
    this.username = username;
    this.scope = scope;
    this.token = token;
    this.creationDate = creationDate;
    this.expirationDate = expirationDate;
    this.consumed = consumed;
    this.revoked = revoked;
  }

  public UserTokenDto username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Username associated with the token.
   * @return username
   */
  @NotNull 
  @Schema(name = "username", description = "Username associated with the token.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("username")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserTokenDto name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * User name.
   * @return name
   */
  
  @Schema(name = "name", description = "User name.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public UserTokenDto scope(String scope) {
    this.scope = scope;
    return this;
  }

  /**
   * Scope for which the token is valid.
   * @return scope
   */
  @NotNull 
  @Schema(name = "scope", description = "Scope for which the token is valid.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("scope")
  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public UserTokenDto token(String token) {
    this.token = token;
    return this;
  }

  /**
   * Unique token identifier.
   * @return token
   */
  @NotNull 
  @Schema(name = "token", description = "Unique token identifier.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("token")
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public UserTokenDto creationDate(Instant creationDate) {
    this.creationDate = creationDate;
    return this;
  }

  /**
   * Get creationDate
   * @return creationDate
   */
  @NotNull @Valid 
  @Schema(name = "creationDate", example = "2025-08-01T00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("creationDate")
  public Instant getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Instant creationDate) {
    this.creationDate = creationDate;
  }

  public UserTokenDto expirationDate(Instant expirationDate) {
    this.expirationDate = expirationDate;
    return this;
  }

  /**
   * Get expirationDate
   * @return expirationDate
   */
  @NotNull @Valid 
  @Schema(name = "expirationDate", example = "2025-08-01T00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("expirationDate")
  public Instant getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(Instant expirationDate) {
    this.expirationDate = expirationDate;
  }

  public UserTokenDto consumed(Boolean consumed) {
    this.consumed = consumed;
    return this;
  }

  /**
   * Indicates whether the token has been consumed.
   * @return consumed
   */
  @NotNull 
  @Schema(name = "consumed", description = "Indicates whether the token has been consumed.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("consumed")
  public Boolean getConsumed() {
    return consumed;
  }

  public void setConsumed(Boolean consumed) {
    this.consumed = consumed;
  }

  public UserTokenDto revoked(Boolean revoked) {
    this.revoked = revoked;
    return this;
  }

  /**
   * Indicates whether the token has been revoked.
   * @return revoked
   */
  @NotNull 
  @Schema(name = "revoked", description = "Indicates whether the token has been revoked.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("revoked")
  public Boolean getRevoked() {
    return revoked;
  }

  public void setRevoked(Boolean revoked) {
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
    UserTokenDto userToken = (UserTokenDto) o;
    return Objects.equals(this.username, userToken.username) &&
        Objects.equals(this.name, userToken.name) &&
        Objects.equals(this.scope, userToken.scope) &&
        Objects.equals(this.token, userToken.token) &&
        Objects.equals(this.creationDate, userToken.creationDate) &&
        Objects.equals(this.expirationDate, userToken.expirationDate) &&
        Objects.equals(this.consumed, userToken.consumed) &&
        Objects.equals(this.revoked, userToken.revoked);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, name, scope, token, creationDate, expirationDate, consumed, revoked);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserTokenDto {\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
    sb.append("    expirationDate: ").append(toIndentedString(expirationDate)).append("\n");
    sb.append("    consumed: ").append(toIndentedString(consumed)).append("\n");
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

