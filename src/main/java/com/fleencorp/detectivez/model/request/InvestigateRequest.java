package com.fleencorp.detectivez.model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public record InvestigateRequest(String websiteLinkOrQuery, String analysis, String otherTextContent) {

  public String query() {
    return websiteLinkOrQuery;
  }

  public static InvestigateRequest of(final String websiteLinkOrQuery, final Map<String, Object> websiteScamScore, final String otherTextContent) {
    final String analysis = "";
    final ObjectMapper objectMapper = new ObjectMapper();

    try { objectMapper.writeValueAsString(websiteScamScore); }
    catch (final JsonProcessingException _) {}

    return new InvestigateRequest(websiteLinkOrQuery, analysis, otherTextContent);
  }
}
