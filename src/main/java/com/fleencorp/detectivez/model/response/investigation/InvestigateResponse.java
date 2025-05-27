package com.fleencorp.detectivez.model.response.investigation;

import com.fasterxml.jackson.annotation.*;
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
@JsonPropertyOrder({
  "content",
  "message"
})
public class InvestigateResponse extends LocalizedResponse {

  @JsonProperty("content")
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
    final String content;

    try {
      final JsonNode rootNode = mapper.readTree(result);
      content = rootNode.get("content").asText();
    } catch (final Exception _) {
      final String errorMessage = "**No content available. An error has occurred.**";
      log.info(errorMessage);
      return new InvestigateResponse(errorMessage);
    }

    return new InvestigateResponse(content, result);
  }
}
