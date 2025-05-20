package com.fleencorp.detectivez.adapter.base.service;

import com.fleencorp.detectivez.model.request.InvestigateRequest;
import com.fleencorp.detectivez.model.response.external.ExternalInvestigateResponse;
import reactor.core.publisher.Mono;

public interface InvestigationAdapter {

  Mono<ExternalInvestigateResponse> investigate(InvestigateRequest request);
}
