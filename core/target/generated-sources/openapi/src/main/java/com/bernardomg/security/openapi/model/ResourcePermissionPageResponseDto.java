package com.bernardomg.security.openapi.model;

import java.net.URI;
import java.util.Objects;
import com.bernardomg.security.openapi.model.ResourcePermissionDto;
import com.bernardomg.security.openapi.model.SortingDto;
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
 * ResourcePermissionPageResponseDto
 */

@JsonTypeName("ResourcePermissionPageResponse")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class ResourcePermissionPageResponseDto {

  @Valid
  private List<@Valid ResourcePermissionDto> content = new ArrayList<>();

  private @Nullable Integer size;

  private @Nullable Integer page;

  private @Nullable Long totalElements;

  private @Nullable Long totalPages;

  private @Nullable Integer elementsInPage;

  private @Nullable Boolean first;

  private @Nullable Boolean last;

  private @Nullable SortingDto sort;

  public ResourcePermissionPageResponseDto content(List<@Valid ResourcePermissionDto> content) {
    this.content = content;
    return this;
  }

  public ResourcePermissionPageResponseDto addContentItem(ResourcePermissionDto contentItem) {
    if (this.content == null) {
      this.content = new ArrayList<>();
    }
    this.content.add(contentItem);
    return this;
  }

  /**
   * Get content
   * @return content
   */
  @Valid 
  @Schema(name = "content", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("content")
  public List<@Valid ResourcePermissionDto> getContent() {
    return content;
  }

  public void setContent(List<@Valid ResourcePermissionDto> content) {
    this.content = content;
  }

  public ResourcePermissionPageResponseDto size(@Nullable Integer size) {
    this.size = size;
    return this;
  }

  /**
   * Get size
   * @return size
   */
  
  @Schema(name = "size", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("size")
  public @Nullable Integer getSize() {
    return size;
  }

  public void setSize(@Nullable Integer size) {
    this.size = size;
  }

  public ResourcePermissionPageResponseDto page(@Nullable Integer page) {
    this.page = page;
    return this;
  }

  /**
   * Get page
   * @return page
   */
  
  @Schema(name = "page", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("page")
  public @Nullable Integer getPage() {
    return page;
  }

  public void setPage(@Nullable Integer page) {
    this.page = page;
  }

  public ResourcePermissionPageResponseDto totalElements(@Nullable Long totalElements) {
    this.totalElements = totalElements;
    return this;
  }

  /**
   * Get totalElements
   * @return totalElements
   */
  
  @Schema(name = "totalElements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalElements")
  public @Nullable Long getTotalElements() {
    return totalElements;
  }

  public void setTotalElements(@Nullable Long totalElements) {
    this.totalElements = totalElements;
  }

  public ResourcePermissionPageResponseDto totalPages(@Nullable Long totalPages) {
    this.totalPages = totalPages;
    return this;
  }

  /**
   * Get totalPages
   * @return totalPages
   */
  
  @Schema(name = "totalPages", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalPages")
  public @Nullable Long getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(@Nullable Long totalPages) {
    this.totalPages = totalPages;
  }

  public ResourcePermissionPageResponseDto elementsInPage(@Nullable Integer elementsInPage) {
    this.elementsInPage = elementsInPage;
    return this;
  }

  /**
   * Get elementsInPage
   * @return elementsInPage
   */
  
  @Schema(name = "elementsInPage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("elementsInPage")
  public @Nullable Integer getElementsInPage() {
    return elementsInPage;
  }

  public void setElementsInPage(@Nullable Integer elementsInPage) {
    this.elementsInPage = elementsInPage;
  }

  public ResourcePermissionPageResponseDto first(@Nullable Boolean first) {
    this.first = first;
    return this;
  }

  /**
   * Get first
   * @return first
   */
  
  @Schema(name = "first", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("first")
  public @Nullable Boolean getFirst() {
    return first;
  }

  public void setFirst(@Nullable Boolean first) {
    this.first = first;
  }

  public ResourcePermissionPageResponseDto last(@Nullable Boolean last) {
    this.last = last;
    return this;
  }

  /**
   * Get last
   * @return last
   */
  
  @Schema(name = "last", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last")
  public @Nullable Boolean getLast() {
    return last;
  }

  public void setLast(@Nullable Boolean last) {
    this.last = last;
  }

  public ResourcePermissionPageResponseDto sort(@Nullable SortingDto sort) {
    this.sort = sort;
    return this;
  }

  /**
   * Get sort
   * @return sort
   */
  @Valid 
  @Schema(name = "sort", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sort")
  public @Nullable SortingDto getSort() {
    return sort;
  }

  public void setSort(@Nullable SortingDto sort) {
    this.sort = sort;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResourcePermissionPageResponseDto resourcePermissionPageResponse = (ResourcePermissionPageResponseDto) o;
    return Objects.equals(this.content, resourcePermissionPageResponse.content) &&
        Objects.equals(this.size, resourcePermissionPageResponse.size) &&
        Objects.equals(this.page, resourcePermissionPageResponse.page) &&
        Objects.equals(this.totalElements, resourcePermissionPageResponse.totalElements) &&
        Objects.equals(this.totalPages, resourcePermissionPageResponse.totalPages) &&
        Objects.equals(this.elementsInPage, resourcePermissionPageResponse.elementsInPage) &&
        Objects.equals(this.first, resourcePermissionPageResponse.first) &&
        Objects.equals(this.last, resourcePermissionPageResponse.last) &&
        Objects.equals(this.sort, resourcePermissionPageResponse.sort);
  }

  @Override
  public int hashCode() {
    return Objects.hash(content, size, page, totalElements, totalPages, elementsInPage, first, last, sort);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResourcePermissionPageResponseDto {\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    totalElements: ").append(toIndentedString(totalElements)).append("\n");
    sb.append("    totalPages: ").append(toIndentedString(totalPages)).append("\n");
    sb.append("    elementsInPage: ").append(toIndentedString(elementsInPage)).append("\n");
    sb.append("    first: ").append(toIndentedString(first)).append("\n");
    sb.append("    last: ").append(toIndentedString(last)).append("\n");
    sb.append("    sort: ").append(toIndentedString(sort)).append("\n");
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

