package com.fleencorp.detectivez.adapter.openai;

import com.fleencorp.detectivez.model.response.external.ExternalInvestigateResponse;
import reactor.core.publisher.Mono;

public interface OpenAiAdapter {

  Mono<ExternalInvestigateResponse> analyseAndReport(String query);
}
