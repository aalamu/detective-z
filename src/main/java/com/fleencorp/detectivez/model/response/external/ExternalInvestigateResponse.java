package com.fleencorp.detectivez.model.response.external;

public record ExternalInvestigateResponse(String response) {

  public static ExternalInvestigateResponse of(final String response) {
    return new ExternalInvestigateResponse(response);
  }
}
