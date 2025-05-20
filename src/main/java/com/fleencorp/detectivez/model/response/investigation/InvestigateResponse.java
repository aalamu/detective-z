package com.fleencorp.detectivez.model.response.investigation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fleencorp.localizer.model.response.LocalizedResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvestigateResponse extends LocalizedResponse {

  @JsonProperty("result")
  private String result;

  @JsonIgnore
  private String rawContent;

  @Override
  public String getMessageCode() {
    return "investigate";
  }

  public InvestigateResponse(final String result) {
    this.result = result;
    this.rawContent = null;
  }

  public InvestigateResponse(final String result, final String rawContent) {
    this.result = result;
    this.rawContent = rawContent;
  }

  public static InvestigateResponse of(final String result) {
    final ObjectMapper mapper = new ObjectMapper();
    String content = "";

    try {
      final JsonNode rootNode = mapper.readTree(result);
      content = rootNode.get("content").asText();
    } catch (final Exception _) {
      log.info("Error occurred");
      return new InvestigateResponse(content);
    }

    return new InvestigateResponse(content, result);
  }
}
