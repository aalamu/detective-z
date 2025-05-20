package com.fleencorp.detectivez.service;

import com.fleencorp.detectivez.model.dto.InvestigateDto;
import com.fleencorp.detectivez.model.response.investigation.InvestigateResponse;
import reactor.core.publisher.Mono;

public interface InvestigationService {

  Mono<InvestigateResponse> investigate(InvestigateDto investigateDto);
}
