package com.elasticsearch.search.api.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Result
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-05-11T14:34:18.449563942-03:00[America/Sao_Paulo]")

public class Result  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("title")
  private String title;

  @JsonProperty("url")
  private String url;

  @JsonProperty("abs")
  private String abs;

  @JsonProperty("score")
  private Double score;

  @JsonProperty("highlights")
  @Valid
  private List<String> highlights = null;

  public Result title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
  */
  @ApiModelProperty(value = "")


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Result url(String url) {
    this.url = url;
    return this;
  }

  /**
   * Get url
   * @return url
  */
  @ApiModelProperty(value = "")


  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Result abs(String abs) {
    this.abs = abs;
    return this;
  }

  /**
   * Get abs
   * @return abs
  */
  @ApiModelProperty(value = "")


  public String getAbs() {
    return abs;
  }

  public void setAbs(String abs) {
    this.abs = abs;
  }

  public Result score(Double score) {
    this.score = score;
    return this;
  }

  /**
   * Relevance score returned by Elasticsearch
   * @return score
  */
  @ApiModelProperty(value = "Relevance score returned by Elasticsearch")


  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  public Result highlights(List<String> highlights) {
    this.highlights = highlights;
    return this;
  }

  public Result addHighlightsItem(String highlightsItem) {
    if (this.highlights == null) {
      this.highlights = new ArrayList<>();
    }
    this.highlights.add(highlightsItem);
    return this;
  }

  /**
   * Text fragments highlighted by Elasticsearch
   * @return highlights
  */
  @ApiModelProperty(value = "Text fragments highlighted by Elasticsearch")


  public List<String> getHighlights() {
    return highlights;
  }

  public void setHighlights(List<String> highlights) {
    this.highlights = highlights;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Result result = (Result) o;
    return Objects.equals(this.title, result.title) &&
        Objects.equals(this.url, result.url) &&
        Objects.equals(this.abs, result.abs) &&
        Objects.equals(this.score, result.score) &&
        Objects.equals(this.highlights, result.highlights);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, url, abs, score, highlights);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Result {\n");
    
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    abs: ").append(toIndentedString(abs)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    highlights: ").append(toIndentedString(highlights)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

