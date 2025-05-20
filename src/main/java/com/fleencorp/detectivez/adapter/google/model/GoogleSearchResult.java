package com.fleencorp.detectivez.adapter.google.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoogleSearchResult {

  private Queries queries;
  private List<GoogleSearchResultItem> items;

  @Getter
  @Setter
  public static class Queries {

    @JsonProperty("request")
    private List<QueryPage> request;

    @JsonProperty("nextPage")
    private List<QueryPage> nextPage;

    @Getter
    @Setter
    public static class QueryPage {

      @JsonProperty("title")
      private String title;

      @JsonProperty("totalResults")
      private String totalResults;

      @JsonProperty("searchTerms")
      private String searchTerms;

      @JsonProperty("count")
      private int count;

      @JsonProperty("startIndex")
      private int startIndex;

      @JsonProperty("inputEncoding")
      private String inputEncoding;

      @JsonProperty("outputEncoding")
      private String outputEncoding;

      @JsonProperty("safe")
      private String safe;

      @JsonProperty("cx")
      private String cx;
    }
  }
}
