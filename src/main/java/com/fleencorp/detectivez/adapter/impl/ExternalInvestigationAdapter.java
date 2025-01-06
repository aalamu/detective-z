package com.fleencorp.detectivez.adapter.impl;

import com.fleencorp.detectivez.adapter.InvestigationAdapter;
import com.fleencorp.detectivez.model.request.InvestigateRequest;
import com.fleencorp.detectivez.model.response.external.ExternalInvestigateResponse;

public class ExternalInvestigationAdapter implements InvestigationAdapter {

  public ExternalInvestigateResponse investigate(InvestigateRequest investigateRequest) {
    return new ExternalInvestigateResponse(null);
  }
}
